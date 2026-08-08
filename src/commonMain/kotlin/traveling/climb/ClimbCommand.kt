package traveling.climb

import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
import core.utility.toNameSearchableList
import traveling.direction.Direction

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
	Climb <direction> - Climb in <direction>
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
        val delimiters = listOf(ArgDelimiter(listOf("of", "to")))
        val arguments = Args(args, delimiters)
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
        val desiredDirection = arguments.getDirection().let { if (it == Direction.NONE) Direction.ABOVE else it }
        val thingName = if (arguments.getString("to") != "") {
            arguments.getString("to")
        } else {
            arguments.getBaseString()
        }.replace(desiredDirection.name.lowercase(), "").trim()
        val things = player.location.determineClimbThings().filter { it.getDirection(source) == desiredDirection }
        val matchThing = things.map { it.thing }.toNameSearchableList().getOrNull(thingName)
        val matchByName = things.firstOrNull { it.thing == matchThing }

        val confidentMatch = matchByName ?: things.takeIf { it.size == 1 }?.first()
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

    private suspend fun clarifyClimbThing(player: Player, options: List<ClimbThing>, desiredDirection: Direction) {
        when {
            options.isEmpty() -> player.displayToMe("There doesn't seem to be anything to climb.")
            options.size == 1 && desiredDirection != Direction.NONE -> CommandParsers.parseCommand(
                player,
                "climb $desiredDirection ${options[0]}"
            )


            options.size == 1 -> CommandParsers.parseCommand(player, "climb ${options[0]}")
            desiredDirection != Direction.NONE -> player.respond("There is nothing to climb.") {
                message("Climb what?")
                displayedOptions(options.map { it.thing.name })
                options(options.map { "climb ${it.thing.name}" })
            }

            else -> player.respond("There is nothing to climb.") {
                message("Climb what?")
                optionsNamed(options.map { it.thing })
                command { "climb $it" }
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
