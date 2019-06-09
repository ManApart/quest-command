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

    private fun getChance(creature: Target, segmentDistance: Int): Double {
        //TODO - segment difficulty by material
        val skill = creature.soul.getCurrent(CLIMBING)
        val segmentDifficulty = 1
        val challenge = Math.max(segmentDistance * segmentDifficulty, 1)
        return skill / challenge.toDouble()
    }

    private fun advance(event: AttemptClimbEvent, distance: Int, chance: Double) {
        val directionString = getDirectionString(event.desiredDirection)
        if (distance == 0) {
            display("You climb ${event.targetPart.name}.")
        } else {
            display("You climb $distance ft$directionString towards ${event.targetPart.name}.")
        }

        GameState.player.setClimbing(event.target)
        awardEXP(GameState.player, chance)

        if (isDemountableEdgeNode(event)) {
            val connectedLocation = getConnectedLocation(event.target.location, event.target, event.targetPart)
            if (creatureIsComingFromConnection(event, connectedLocation)) {
                dismountFromConnection(event, connectedLocation)
            } else {
                dismountToConnection(event, connectedLocation)
            }
        } else {
            continueClimbing(event)
        }
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

    private fun getDirectionString(direction: Direction): String {
        return if (direction == Direction.NONE) {
            ""
        } else {
            " " + direction.name
        }
    }

    /**
     * Target part is at the edge of the network (in the desired direction) and is either 0 feet from the ground or has a connected exit)
     */
    private fun isDemountableEdgeNode(event: AttemptClimbEvent): Boolean {
        return event.targetPart.isAnOuterNode(event.desiredDirection)
                && (event.targetPart.getDistanceToLowestNodeInNetwork() == 0 || getConnectedLocation(event.target.location, event.target, event.targetPart) != null)
    }

    private fun getConnectedLocation(targetLocation: LocationNode, climbTarget: Target, part: LocationNode): LocationPoint? {
        return targetLocation.getNeighborConnections()
                .firstOrNull { it.source.equals(targetLocation, climbTarget, climbTarget.body.getPart(part.name)) }
                ?.destination
    }

    private fun creatureIsComingFromConnection(event: AttemptClimbEvent, connectedLocation: LocationPoint?): Boolean {
        return connectedLocation?.location == event.creature.location
    }


    private fun continueClimbing(event: AttemptClimbEvent) {
        EventManager.postEvent(ArriveEvent(event.creature, LocationPoint(event.creature.location), LocationPoint(event.targetPart), "Climb", true))
        EventManager.postEvent(LookEvent())
    }

    private fun fall(event: AttemptClimbEvent) {
        EventManager.postEvent(FallEvent(event.creature, event.target.location, event.creature.location.getDistanceToLowestNodeInNetwork(), "You lose your grip on ${event.targetPart.name}."))
    }

    private fun dismountFromConnection(event: AttemptClimbEvent, connectedLocation: LocationPoint?) {
        val destination = LocationPoint(event.target.location)
        val origin = connectedLocation ?: LocationPoint(event.target.location, event.target.name, event.targetPart.name)

        EventManager.postEvent(ClimbCompleteEvent(event.creature, event.target, origin, destination))
    }

    private fun dismountToConnection(event: AttemptClimbEvent, connectedLocation: LocationPoint?) {
        val origin = LocationPoint(event.target.location, event.target.name, event.targetPart.name)
        val destination = connectedLocation ?: LocationPoint(event.target.location)

        EventManager.postEvent(ClimbCompleteEvent(event.creature, event.target, origin, destination))
    }

}