package traveling.travel

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.CommandParsers
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import traveling.direction.Direction
import traveling.location.network.LocationNode

class TravelInDirectionCommand : Command() {
    override fun getAliases(): List<String> {
        val aliases = mutableListOf("Direction")
        Direction.values().forEach {
            aliases.add(it.name)
            aliases.add(it.shortcut)
        }
        return aliases
    }

    override fun getDescription(): String {
        return "Move to the nearest location in the specified direction."
    }

    override fun getManual(): String {
        return """
	<direction> - Start moving to the nearest location in that direction, if it exists.
	<direction> s - The s flag silences travel, meaning a minimum amount of output"""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return Direction.values().map { it.name }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        if (keyword.lowercase() == "direction") {
            clarifyDirection(source)
        } else {
            val direction = Direction.getDirection(keyword)
            when {
                direction == Direction.NONE -> source.displayToMe("Could not find direction $keyword")
                source.properties.values.getBoolean(IS_CLIMBING) -> CommandParsers.parseCommand(source, "climb $direction")
                else -> {
                    val neighbors = source.location.getNeighbors(direction)
                    val openNeighbors = neighbors.filter { !source.location.isMovingToRestricted(it) }
                    val quiet = Args(args).hasFlag("s")
                    val quietFlag = if (quiet){"s"} else {""}

                    when {
                        openNeighbors.size == 1 -> EventManager.postEvent(TravelStartEvent(source.thing, destination = openNeighbors.first(), quiet = quiet))
                        openNeighbors.size > 1 -> requestLocation(source, openNeighbors)
                        openNeighbors.isEmpty() && neighbors.isNotEmpty() -> CommandParsers.parseCommand(source, "climb $direction $quietFlag")
                        else -> source.displayToMe("Could not find a location to the $direction.")
                    }
                }
            }
        }
    }

    private fun clarifyDirection(source: Player) {
        source.respond({}) {
            message("Travel in which direction?")
            options(Direction.values().map { it.name })
        }
    }

    private fun requestLocation(source: Player, openNeighbors: List<LocationNode>) {
        source.respond("There aren't any locations to travel towards.") {
            message("Travel towards what location?")
            optionsNamed(openNeighbors)
            command { "travel $it" }
        }
    }


}