package traveling.climb

import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
import core.utility.takeIfOne
import core.utility.toNameSearchableList
import traveling.direction.Direction
import kotlin.text.contains

class ClimbCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("Climb", "cl", "scale", "descend")
    }

    override fun getDescription(): String {
        return "Climb over obstacles"
    }

    override fun getManual(): String {
        return """
	Climb <thing> - Climb (onto) the thing
	Climb <Above/Below> - Climb in <direction>
	Climb to <location> - Climb to a location
	Climb s - The s flag silences travel, meaning a minimum amount of output"""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() && source.thing.climbThing == null -> source.getPerceivedThings().filter { it.properties.tags.has("Climbable") }.map { it.name }
            args.isEmpty() -> Direction.entries.map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = args(args, "to")
        when {
            source.thing.getEncumbrance() >= 1 -> source.displayToMe("You are too encumbered to climb.")
            source.thing.climbThing != null && source.properties.values.getBoolean(IS_CLIMBING) -> processClimbing(
                source,
                keyword,
                arguments,
                source.thing.climbThing!!
            )

            else -> processNewClimb(source, keyword, arguments)
        }
    }

    private suspend fun processNewClimb(player: Player, keyword: String, arguments: Args) {
        val source = player.thing
        val desiredDirection = arguments.getDirectionFromBase().let { if (it == Direction.NONE) Direction.ABOVE else it }
        val toName = arguments.getString("to").takeIf { it.isNotBlank() }
        val thingName = arguments.getBaseString().replace(desiredDirection.name.lowercase(), "").trim()

        val things = player.location.determineClimbThings().filter { it.getDirection(source) == desiredDirection }.hasToName(toName)
        val matchThings = things.map { it.thing }.toNameSearchableList().getAll(thingName)
        val matchesByName = things.filter { matchThings.contains(it.thing) }.hasToName(toName)

        val confidentMatch = matchesByName.takeIfOne() ?: things.takeIfOne()
        val quiet = arguments.hasFlag("s")

        if (confidentMatch != null) {
            if (!confidentMatch.thing.properties.tags.has("Climbable")) {
                source.displayToMe("${confidentMatch.thing.name} cannot be climbed.")
            } else {
                EventManager.postEvent(
                    AttemptClimbEvent(source, confidentMatch, desiredDirection, quiet)
                )
            }
        } else if (things.size == 1 && isAlias(keyword)) {
            val match = things.first()
            EventManager.postEvent(
                AttemptClimbEvent(source, match, desiredDirection, quiet)
            )
        } else {
            clarifyClimbThing(player, things, desiredDirection)
        }
    }

    private fun List<ClimbThing>.hasToName(toName: String?) = if (toName == null) this else filter {
        it.exit?.location?.name?.lowercase()?.contains(toName) == true
    }

    private suspend fun clarifyClimbThing(player: Player, options: List<ClimbThing>, desiredDirection: Direction) {
        when {
            options.isEmpty() -> player.displayToMe("There doesn't seem to be anything to climb.")
            options.size == 1 && desiredDirection != Direction.NONE -> CommandParsers.parseCommand(
                player,
                "climb $desiredDirection ${options[0]}"
            )

            options.size == 1 -> CommandParsers.parseCommand(player, "climb ${options[0]}")
            else -> player.respond("There is nothing to climb.") {
                message("Climb what?")
                options(options.map { "climb ${it.getName("to")}" })
            }
        }
    }

    private fun processClimbing(player: Player, keyword: String, arguments: Args, thing: Thing) {
        val direction = arguments.getDirection()
        when {
            arguments.isEmpty() -> {
                val keywordDirection = if (keyword == "descend") {
                    Direction.BELOW
                } else {
                    Direction.ABOVE
                }
                EventManager.postEvent(AttemptClimbEvent(player.thing, ClimbThing(thing), keywordDirection))
            }

            else -> EventManager.postEvent(AttemptClimbEvent(player.thing, ClimbThing(thing), direction))
        }
    }

}
