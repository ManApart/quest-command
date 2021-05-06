package traveling.travel

import core.GameState
import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.properties.IS_CLIMBING
import traveling.direction.Direction
import traveling.location.location.LocationNode

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

    override fun execute(keyword: String, args: List<String>) {
        if (keyword.lowercase() == "direction") {
            clarifyDirection()
        } else {
            val direction = Direction.getDirection(keyword)
            when {
                direction == Direction.NONE -> display("Could not find direction $keyword")
                GameState.player.properties.values.getBoolean(IS_CLIMBING) -> CommandParser.parseCommand("climb $direction")
                else -> {
                    val neighbors = GameState.player.location.getNeighbors(direction)
                    val openNeighbors = neighbors.filter { !GameState.player.location.isMovingToRestricted(it) }
                    val quiet = Args(args).hasFlag("s")
                    val quietFlag = if (quiet){"s"} else {""}

                    when {
                        openNeighbors.size == 1 -> EventManager.postEvent(TravelStartEvent(destination = openNeighbors.first(), quiet = quiet))
                        openNeighbors.size > 1 -> requestLocation(openNeighbors)
                        openNeighbors.isEmpty() && neighbors.isNotEmpty() -> CommandParser.parseCommand("climb $direction $quietFlag")
                        else -> display("Could not find a location to the $direction.")
                    }
                }
            }
        }
    }

    private fun clarifyDirection() {
        val targets = Direction.values().map { it.name }
        val message = "Travel in which direction?"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { it }))
    }

    private fun requestLocation(openNeighbors: List<LocationNode>) {
        val message = "Travel towards what location?\n\t${openNeighbors.joinToString(", ")}"
        val response = ResponseRequest(message, openNeighbors.associate { it.name to "travel ${it.name}" })
         CommandParser.setResponseRequest(response)
    }


}