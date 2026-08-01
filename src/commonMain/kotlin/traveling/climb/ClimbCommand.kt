package traveling.climb

import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
import core.utility.NameSearchableList
import traveling.direction.Direction

class ClimbCommand : Command() {

    private class ClimbOption(val thing: Thing, val direction: Direction)

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
            source.properties.values.getBoolean(IS_CLIMBING) -> processClimbing(
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
        val thingName = if (arguments.getString("to") != "") {
            arguments.getString("to")
        } else {
            arguments.getBaseString()
        }
        val things = findAllThings(source)
        val desiredDirection = arguments.getDirection().let { if (it == Direction.NONE) Direction.ABOVE else it }
        val matchByName = things.getOrNull(thingName)
        val quiet = arguments.hasFlag("s")

        if (matchByName != null) {
            if (!matchByName.properties.tags.has("Climbable")) {
                source.displayToMe("${matchByName.name} cannot be climbed.")
            } else {
                EventManager.postEvent(
                    AttemptClimbEvent(
                        source,
                        matchByName,
                        desiredDirection,
                        quiet
                    )
                )
            }
        } else if (isAlias(keyword) && things.size == 1) {
            val match = things.first()
            EventManager.postEvent(
                AttemptClimbEvent(
                    source,
                    match,
                    desiredDirection,
                    quiet
                )
            )
        } else {
            clarifyClimbThing(player, things, desiredDirection)
        }
    }

    private fun getConfidentMatch(namedMatch: Thing?, directionMatches: List<ClimbOption>): Thing? {
        return namedMatch ?: if (directionMatches.size == 1) {
            directionMatches.first().thing
        } else {
            null
        }
    }

    private suspend fun findAllThings(source: Thing): NameSearchableList<Thing> {
        val localClimbableThings = source.currentLocation().findThingsByTag("Climbable")
        val connections = source.location.getNeighborConnections().filter { connection ->
            localClimbableThings.none { it.name == connection.source.thingName }
        }
        val connectedThings =
            connections.flatMap { it.destination.location.getLocation().getThings(it.destination.thingName!!) }
        return NameSearchableList(localClimbableThings + connectedThings)
    }


    private suspend fun clarifyClimbThing(player: Player, options: NameSearchableList<Thing>, desiredDirection: Direction) {
        when {
            options.isEmpty() -> player.displayToMe("There doesn't seem to be anything to climb.")
            options.size == 1 && desiredDirection != Direction.NONE -> CommandParsers.parseCommand(
                player,
                "climb $desiredDirection ${options[0]}"
            )


            options.size == 1 -> CommandParsers.parseCommand(player, "climb ${options[0]}")
            desiredDirection != Direction.NONE -> player.respond("There is nothing to climb.") {
                message("Climb what?")
                displayedOptions(options.map { it.name })
                options(options.map { "climb ${it.name}" })
            }

            else -> player.respond("There is nothing to climb.") {
                message("Climb what?")
                optionsNamed(options)
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
                EventManager.postEvent(AttemptClimbEvent(player.thing, thing, keywordDirection))
            }

            else -> EventManager.postEvent(AttemptClimbEvent(player.thing, thing, direction))
        }
    }


}
