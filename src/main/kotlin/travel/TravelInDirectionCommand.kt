package travel

import core.commands.Command
import core.gameState.Activator
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.location.LocationNode
import core.history.display
import interact.scope.ScopeManager
import system.EventManager
import travel.climb.ClimbJourney
import travel.climb.ClimbJourneyEvent
import travel.climb.StartClimbingEvent

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

    private fun processClimbing(direction: Direction) {
        if (isClimbing()) {
            val upwards= direction == Direction.ABOVE
            climbStep(GameState.player.climbJourney!!.getNextSegment(upwards))
        } else {
            val found = findLocationInDirection(direction)
            if (found != null && !GameState.player.creature.location.isMovingToRestricted(found)) {
                EventManager.postEvent(TravelStartEvent(destination = found))
            } else {
                val climbTarget = findClimbTarget(direction)
                if (climbTarget != null) {
                    EventManager.postEvent(StartClimbingEvent(GameState.player.creature, climbTarget))
                } else {
                    display("Could not find anything to attemptClimb.")
                }
            }
        }
    }

    private fun isClimbing(): Boolean {
        return GameState.player.climbJourney != null && GameState.player.climbJourney is ClimbJourney
    }

    private fun climbStep(step: Int) {
        if (step != 0) {
            EventManager.postEvent(ClimbJourneyEvent(step))
        } else {
            display("Couldn't find the next place to climb to!")
        }
    }

    private fun findClimbTarget(direction: Direction): Activator? {
        val desireUpwards = direction == Direction.ABOVE
        return ScopeManager.getScope().findTargetsByTag("Climbable")
                .firstOrNull {
                    it is Activator && it.climb != null && it.climb.upwards == desireUpwards
                } as Activator?
    }

    private fun findLocationInDirection(direction: Direction): LocationNode? {
        val loc = GameState.player.creature.location
        return loc.getNeighbors(direction).firstOrNull()
    }


}