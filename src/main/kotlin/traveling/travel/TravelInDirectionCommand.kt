package traveling.travel

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
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

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        if (keyword.lowercase() == "direction") {
            clarifyDirection()
        } else {
            val direction = Direction.getDirection(keyword)
            when {
                direction == Direction.NONE -> source.displayToMe("Could not find direction $keyword")
                source.properties.values.getBoolean(IS_CLIMBING) -> CommandParser.parseCommand("climb $direction")
                else -> {
                    val neighbors = source.location.getNeighbors(direction)
                    val openNeighbors = neighbors.filter { !source.location.isMovingToRestricted(it) }
                    val quiet = Args(args).hasFlag("s")
                    val quietFlag = if (quiet){"s"} else {""}

                    when {
                        openNeighbors.size == 1 -> EventManager.postEvent(TravelStartEvent(source, destination = openNeighbors.first(), quiet = quiet))
                        openNeighbors.size > 1 -> requestLocation(openNeighbors)
                        openNeighbors.isEmpty() && neighbors.isNotEmpty() -> CommandParser.parseCommand("climb $direction $quietFlag")
                        else -> source.displayToMe("Could not find a location to the $direction.")
                    }
                }
            }
        }
    }

    private fun clarifyDirection() {
        val things = Direction.values().map { it.name }
        val message = "Travel in which direction?"
        CommandParsers.setResponseRequest(ResponseRequest(message, things.associateWith { it }))
    }

    private fun requestLocation(openNeighbors: List<LocationNode>) {
        val message = "Travel towards what location?\n\t${openNeighbors.joinToString(", ")}"
        val response = ResponseRequest(message, openNeighbors.associate { it.name to "travel ${it.name}" })
         CommandParsers.setResponseRequest(response)
    }


}