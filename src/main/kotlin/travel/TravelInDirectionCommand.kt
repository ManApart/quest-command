package travel

import core.commands.Command
import core.gameState.Target
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.history.display
import interact.scope.ScopeManager
import system.EventManager
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
            display(getManual())
        } else {
            val direction = Direction.getDirection(keyword)
            if (direction == Direction.NONE) {
                display("Could not find direction $keyword")
            } else if (direction == Direction.ABOVE || direction == Direction.BELOW) {
                processClimbing(direction)
            } else {
                val found = findLocationInDirection(direction)
                if (found != null) {
                    EventManager.postEvent(TravelStartEvent(destination = found))
                } else {
                    display("Could not find a location to the $direction.")
                }
            }
        }
    }

    //TODO Should this just call the climb command?
    private fun processClimbing(direction: Direction) {
        if (GameState.player.isClimbing) {
            climbInDirection(GameState.player.climbTarget!!, direction)
        } else {
            val found = findLocationInDirection(direction)
            if (found != null && !GameState.player.location.isMovingToRestricted(found)) {
                EventManager.postEvent(TravelStartEvent(destination = found))
            } else {
                val climbableTarget = ScopeManager.getScope().findTargetsByTag("Climbable").firstOrNull()
                if (climbableTarget != null) {
                    climbInDirection(climbableTarget, direction)
                } else {
                    display("Could not find anything to climb.")
                }
            }
        }
    }

    private fun climbInDirection(target: Target, direction: Direction) {
        val climbTarget = findLocationInDirection(direction)
        if (climbTarget != null) {
            EventManager.postEvent(AttemptClimbEvent(GameState.player, target, climbTarget))
        } else {
            display("Could not find anything to climb.")
        }
    }

    private fun findLocationInDirection(direction: Direction): LocationNode? {
        val loc = GameState.player.location
        return loc.getNeighbors(direction).firstOrNull()
    }


}