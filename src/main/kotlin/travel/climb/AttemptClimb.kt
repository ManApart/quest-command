package travel.climb

import core.events.EventListener
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.gameState.location.RouteFinder
import core.gameState.stat.CLIMBING
import core.gameState.stat.STAMINA
import core.history.display
import core.utility.RandomManager
import explore.LookEvent
import status.ExpGainedEvent
import status.statChanged.StatChangeEvent
import system.EventManager
import travel.ArriveEvent
import travel.jump.FallEvent

class AttemptClimb : EventListener<AttemptClimbEvent>() {
    override fun shouldExecute(event: AttemptClimbEvent): Boolean {
        return event.creature == GameState.player && event.target.properties.tags.has("Climbable")
    }

    override fun execute(event: AttemptClimbEvent) {
        val distance = getDistance(event.creature.location, event.targetPart)
        val chance = getChance(event.creature, distance)

        EventManager.postEvent(StatChangeEvent(GameState.player, "Climbing", STAMINA, -distance))
        if (RandomManager.isSuccess(chance)) {
            advance(event, distance, chance)
        } else {
            fall(event)
        }
        event.target.consume(event)
    }

    private fun getDistance(source: LocationNode, destination: LocationNode): Int {
        return if (source.parent == destination.parent) {
            val routeFinder = RouteFinder(source, destination)
            if (routeFinder.hasRoute()) {
                routeFinder.getRoute().getDistance()
            } else {
                0
            }
        } else {
            destination.getDistanceToLowestNodeInNetwork()
        }
    }

    private fun advance(event: AttemptClimbEvent, distance: Int, chance: Double) {
        //Extract get direction for climb command and use that
        val direction = getDirection(event.creature.location, event.targetPart)
        val directionString = getDirectionString(direction)
        if (distance == 0){
            display("You climb ${event.targetPart.name}.")
        } else {
            display("You climb $distance ft$directionString towards ${event.targetPart.name}.")
        }

        GameState.player.setClimbing(event.target)
        awardEXP(GameState.player, chance)

        if (isDemountableEdgeNode(event)) {
            val origin = LocationPoint(event.target.location, event.target.name, event.targetPart.name)
            val connectedLocation = getConnectedLocation(event.target.location, event.target, event.targetPart)
            val destination = connectedLocation ?: event.target.location

            EventManager.postEvent(ClimbCompleteEvent(event.creature, event.target, origin, destination))
        } else {
            EventManager.postEvent(ArriveEvent(event.creature, LocationPoint(event.creature.location), LocationPoint(event.targetPart), "Climb", true))
            EventManager.postEvent(LookEvent())
        }
    }

    /**
     * Target part is at the edge of the network (in the desired direction) and is either 0 feet from the ground or has a connected exit)
     */
    private fun isDemountableEdgeNode(event: AttemptClimbEvent): Boolean {
        return event.targetPart.isAnOuterNode(event.desiredDirection)
                && (event.targetPart.getDistanceToLowestNodeInNetwork() == 0 || getConnectedLocation(event.target.location, event.target, event.targetPart) != null)
    }

    private fun getConnectedLocation(targetLocation: LocationNode, climbTarget: Target, part: LocationNode): LocationNode? {
        return targetLocation.getNeighborConnections()
                .firstOrNull { it.source.equals(targetLocation, climbTarget, climbTarget.body.getPart(part.name)) }
                ?.destination?.location
    }

    private fun getDirection(source: LocationNode, destination: LocationNode): Direction {
        if (source.parent == destination.parent) {
            val routeFinder = RouteFinder(source, destination)
            if (routeFinder.hasRoute()) {
                return routeFinder.getRoute().getConnections().first().vector.direction
            }
        }
        return Direction.NONE
    }

    private fun getDirectionString(direction: Direction): String {
        return if (direction == Direction.NONE) {
            ""
        } else {
            " " + direction.name
        }
    }


    private fun getChance(creature: Target, segmentDistance: Int): Double {
        //TODO - segment difficulty by material
        val skill = creature.soul.getCurrent(CLIMBING)
        val segmentDifficulty = 1
        val challenge = Math.max(segmentDistance * segmentDifficulty, 1)
        return skill / challenge.toDouble()
    }

    private fun awardEXP(creature: Target, chance: Double) {
        val amount = if (chance >= 1) {
            0
        } else {
            ((1 - chance) * 100).toInt()
        }
        if (amount > 0) {
            EventManager.postEvent(ExpGainedEvent(creature, CLIMBING, amount))
        }
    }


    private fun fall(event: AttemptClimbEvent) {
        EventManager.postEvent(FallEvent(event.creature, event.target.location, event.creature.location.getDistanceToLowestNodeInNetwork(), "You lose your grip on ${event.targetPart.name}."))
    }

}