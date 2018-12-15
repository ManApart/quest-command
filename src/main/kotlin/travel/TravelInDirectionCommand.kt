package travel

import core.commands.Command
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.history.display
import system.EventManager

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
        return listOf("Travel")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (keyword.toLowerCase() == "direction") {
            display(getManual())
        } else {
            val direction = Direction.getDirection(keyword)
            if (direction == Direction.NONE) {
                display("Could not find direction $keyword")
            } else {
                val found = findLocationInDirection(direction)
                if (found != null) {
                    EventManager.postEvent(TravelStartEvent(destination = found))
                } else {
                    display("Could not find a location to the $direction")
                }
            }
        }
    }

    private fun findLocationInDirection(direction: Direction): LocationNode? {
        val loc = GameState.player.creature.location
        return loc.getNeighbors(direction).firstOrNull()
    }

}