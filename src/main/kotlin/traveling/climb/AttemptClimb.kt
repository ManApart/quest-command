package traveling.climb

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.thing.Thing
import core.utility.RandomManager
import core.utility.asSubject
import core.utility.isAre
import status.ExpGainedEvent
import status.stat.CLIMBING
import status.stat.STAMINA
import status.statChanged.StatChangeEvent
import time.ViewTimeEvent
import traveling.arrive.ArriveEvent
import traveling.direction.Direction
import traveling.jump.FallEvent
import traveling.location.RouteFinder
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import kotlin.math.max

class AttemptClimb : EventListener<AttemptClimbEvent>() {
    override fun shouldExecute(event: AttemptClimbEvent): Boolean {
        return event.creature.isPlayer() && event.thing.properties.tags.has("Climbable")
    }

    override fun execute(event: AttemptClimbEvent) {
        if (!isWithinRange(event)) {
            event.creature.display{event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to climb ${event.thing.name}."}
        } else {
            val distance = getDistance(event.creature.location, event.thingPart)
            val chance = getChance(event.creature, distance)

            EventManager.postEvent(StatChangeEvent(event.creature, "Climbing", STAMINA, -distance, event.quiet))
            if (event.creature.getEncumbrance() < 1f && RandomManager.isSuccess(chance)) {
                advance(event, distance, chance)
            } else {
                fall(event)
            }
            event.thing.consume(event)
        }
    }

    private fun isWithinRange(event: AttemptClimbEvent): Boolean {
        return event.creature.climbThing != null || event.thing.isWithinRangeOf(event.creature)
                || event.thing.location != event.creature.location
    }

    private fun getDistance(source: LocationNode, destination: LocationNode): Int {
        return if (source.parent == destination.parent) {
            val routeFinder = RouteFinder(source, destination)
            if (routeFinder.hasRoute()) {
                routeFinder.getRoute().distance
            } else {
                0
            }
        } else {
            destination.getDistanceToLowestNodeInNetwork()
        }
    }

    private fun getChance(creature: Thing, segmentDistance: Int): Double {
        //TODO - segment difficulty by material
        val skill = creature.soul.getCurrent(CLIMBING)
        val segmentDifficulty = 1
        val challenge = max(segmentDistance * segmentDifficulty, 1)
        return skill / challenge.toDouble()
    }

    private fun advance(event: AttemptClimbEvent, distance: Int, chance: Double) {
        val directionString = getDirectionString(event.desiredDirection)
        when {
            distance == 0 && event.desiredDirection == Direction.BELOW -> event.creature.display("You descend ${event.thingPart.name}.")
            distance == 0 -> event.creature.display("You climb ${event.thingPart.name}.")
            else -> event.creature.display("You climb $distance ft$directionString towards ${event.thingPart.name}.")
        }

        event.creature.setClimbing(event.thing)
        awardEXP(event.creature, chance)

        if (isDemountableEdgeNode(event)) {
            val connectedLocation = getConnectedLocation(event.thing.location, event.thing, event.thingPart)
            if (creatureIsComingFromConnection(event, connectedLocation)) {
                dismountFromConnection(event, connectedLocation)
            } else {
                dismountToConnection(event, connectedLocation)
            }
        } else {
            continueClimbing(event)
        }
    }

    private fun awardEXP(creature: Thing, chance: Double) {
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
     * Thing part is at the edge of the network (in the desired direction) and is either 0 feet from the ground or has a connected exit)
     */
    private fun isDemountableEdgeNode(event: AttemptClimbEvent): Boolean {
        return event.thingPart.isAnOuterNode(event.desiredDirection)
                && (event.thingPart.getDistanceToLowestNodeInNetwork() == 0 || getConnectedLocation(event.thing.location, event.thing, event.thingPart) != null)
    }

    private fun getConnectedLocation(thingLocation: LocationNode, climbThing: Thing, part: LocationNode): LocationPoint? {
        return thingLocation.getNeighborConnections()
            .firstOrNull { it.source.equals(thingLocation, climbThing, climbThing.body.getPart(part.name)) }
            ?.destination
    }

    private fun creatureIsComingFromConnection(event: AttemptClimbEvent, connectedLocation: LocationPoint?): Boolean {
        return connectedLocation?.location == event.creature.location
    }


    private fun continueClimbing(event: AttemptClimbEvent) {
        EventManager.postEvent(ArriveEvent(event.creature, LocationPoint(event.creature.location), LocationPoint(event.thingPart), "Climb", silent = true))
        EventManager.postEvent(ViewTimeEvent(event.creature))
    }

    private fun fall(event: AttemptClimbEvent) {
        EventManager.postEvent(FallEvent(event.creature, event.thing.location, event.creature.location.getDistanceToLowestNodeInNetwork(), "You lose your grip on ${event.thingPart.name}."))
    }

    private fun dismountFromConnection(event: AttemptClimbEvent, connectedLocation: LocationPoint?) {
        val destination = LocationPoint(event.thing.location)
        val origin = connectedLocation ?: LocationPoint(event.thing.location, NO_VECTOR, event.thing.name, event.thingPart.name)

        EventManager.postEvent(ClimbCompleteEvent(event.creature, event.thing, origin, destination))
    }

    private fun dismountToConnection(event: AttemptClimbEvent, connectedLocation: LocationPoint?) {
        val origin = LocationPoint(event.thing.location, NO_VECTOR, event.thing.name, event.thingPart.name)
        val destination = connectedLocation ?: LocationPoint(event.thing.location)

        EventManager.postEvent(ClimbCompleteEvent(event.creature, event.thing, origin, destination))
    }

}