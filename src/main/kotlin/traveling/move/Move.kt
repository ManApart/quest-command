package traveling.move

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.isAre
import core.utility.then
import status.stat.STAMINA
import status.stat.getMaxPossibleMovement
import status.stat.getStaminaCost
import status.statChanged.StatChangeEvent
import traveling.direction.Direction
import traveling.location.Connection
import traveling.location.network.LocationNode
import traveling.position.Distances.LOCATION_SIZE
import traveling.position.NO_VECTOR
import traveling.position.Vector
import traveling.travel.postArriveEvent
import kotlin.math.min

class Move : EventListener<MoveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: MoveEvent) {
        val desiredDistance = event.source.getDistance(event.destination)
        val actualDistance = getActualDistanceMoved(event, desiredDistance)
        val attainableDestination = event.source.getVectorInDirection(event.destination, actualDistance)
        val connection = getMovedToConnection(event.creature.location, event.creature.position, attainableDestination)
        val movedToNeighbor = connection?.destination
        val actualDestination = event.creature.position.getNearest(attainableDestination, connection?.source?.vector ?: attainableDestination)
        val vector = actualDestination - event.creature.position
        val stamina = event.creature.soul.getCurrent(STAMINA)
        val staminaRequired = vector.getDistance() / 10

        when {
            actualDestination.z > 0 -> event.creature.display { "${event.creature.asSubject(it)} ${event.creature.isAre(it)} unable to move into the air." }
            stamina == 0 -> event.creature.display { "${event.creature.asSubject(it)} ${event.creature.isAre(it)} too tired to move." }
            movedToNeighbor != null -> postArriveEvent(event.creature, movedToNeighbor, staminaRequired, event.silent)
            //TODO - location size needs to be calculated by the location
            event.destination.getDistance() > LOCATION_SIZE -> event.creature.displayToMe("You cannot move that far in that direction.")
            else -> move(event, desiredDistance, actualDistance, actualDestination)
        }
    }

    private fun getActualDistanceMoved(event: MoveEvent, desiredDistance: Int): Int {
        val abilityToMove = event.creature.getMaxPossibleMovement(event.staminaScalar)
        return if (event.staminaScalar != 0f) {
            min(abilityToMove, desiredDistance)
        } else {
            desiredDistance
        }
    }

    private fun getMovedToConnection(locationNode: LocationNode, source: Vector, destination: Vector): Connection? {
        //Only find connection when moving outwards
        if (NO_VECTOR.isNearer(destination, source)) return null

        return locationNode.getNeighborConnections()
            .filter {
                it.source.vector.direction != Direction.NONE && destination.isFurtherAlongSameDirectionThan(it.source.vector)
            }
            .minByOrNull { destination.getDistance(it.source.vector) }
    }

    private fun move(event: MoveEvent, desiredDistance: Int, actualDistance: Int, actualDestination: Vector) {
        event.creature.position = actualDestination
        displayMovement(event, desiredDistance, actualDistance, actualDestination)
        if (event.staminaScalar != 0f) {
            EventManager.postEvent(StatChangeEvent(event.creature, "moving", STAMINA, -getStaminaCost(actualDistance, event.staminaScalar), true))
        }
    }

    private fun displayMovement(event: MoveEvent, desiredDistance: Int, actualDistance: Int, actualDestination: Vector) {
        if (!event.silent) {
            val destinationThing = event.creature.location.getLocation().getThings(event.creature).firstOrNull { it != event.creature && it.position == actualDestination }
            val destinationString = destinationThing?.getDisplayName() ?: actualDestination.toString()

            val youMove = event.creature.isPlayer().then("You move", "${event.creature} moves")

            if (desiredDistance == actualDistance) {
                event.creature.display { "$youMove from ${event.source} to $destinationString." }
            } else {
                event.creature.display { "$youMove $actualDistance towards ${event.destination} and ${event.creature.isAre(it)} now at ${destinationString}." }
            }
        }
    }

}