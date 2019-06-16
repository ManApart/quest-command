package travel

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.Target
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.history.display
import interact.scope.ScopeManager
import system.EventManager
import system.help.getCommandGroups
import travel.climb.AttemptClimbEvent

class TravelInDirectionCommand : Command() {
    override fun getAliases(): Array<String> {
        val aliases = mutableListOf("Direction")
        Direction.values().forEach {
            aliases.add(it.name)
            aliases.add(it.shortcut)
        }
        return aliases.toTypedArray()
    }

    override fun getDescription(): String {
        return "Direction:\n\tMove to the nearest location in the specified direction."
    }

    override fun getManual(): String {
        return "\n\t<direction> - Start moving to the nearest location in that direction, if it exists."
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (keyword.toLowerCase() == "direction") {
            clarifyDirection()
        } else {
            val direction = Direction.getDirection(keyword)
            when {
                direction == Direction.NONE -> display("Could not find direction $keyword")
                GameState.player.isClimbing -> CommandParser.parseCommand("climb $direction")
                else -> {
                    val neighbors = GameState.player.location.getNeighbors(direction)
                    val openNeighbors = neighbors.filter { !GameState.player.location.isMovingToRestricted(it) }

                    when {
                        openNeighbors.size == 1 -> EventManager.postEvent(TravelStartEvent(destination = openNeighbors.first()))
                        openNeighbors.size > 1 -> requestLocation(openNeighbors)
                        openNeighbors.isEmpty() && neighbors.isNotEmpty() -> CommandParser.parseCommand("climb $direction")
                        else -> display("Could not find a location to the $direction.")
                    }
                }
            }
        }
    }

    private fun clarifyDirection() {
        val targets = Direction.values().map { it.name }
        display("Travel in which direction?")
        CommandParser.responseRequest = ResponseRequest(targets.map { it to it }.toMap())
    }

    private fun requestLocation(openNeighbors: List<LocationNode>) {
        display("Travel towards what location?\n\t${openNeighbors.joinToString(", ")}")
        val response = ResponseRequest(openNeighbors.map { it.name to "travel ${it.name}" }.toMap())
        CommandParser.responseRequest  = response
    }


}