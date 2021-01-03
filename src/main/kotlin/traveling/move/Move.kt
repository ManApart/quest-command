package traveling.move

import traveling.position.Distances
import traveling.position.Distances.LOCATION_SIZE
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.StringFormatter
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject
import status.stat.STAMINA
import status.statChanged.StatChangeEvent
import traveling.position.NO_VECTOR
import traveling.position.Vector
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint
import traveling.travel.getDistanceToNeighbor
import traveling.travel.postArriveEvent
import kotlin.math.min
import kotlin.math.roundToInt

class Move : EventListener<MoveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: MoveEvent) {
        val desiredDistance = event.source.getDistance(event.destination)
        val actualDistance = getActualDistanceMoved(event, desiredDistance)
        val actualDestination = event.source.getVectorInDirection(event.destination, actualDistance)
        val movedToNeighbor = getMovedToNeighbor(event.creature.location, actualDestination)

        when {
            actualDestination.z > 0 -> display("${getSubject(event.creature)} ${getIsAre(event.creature)} unable to move into the air.")
            movedToNeighbor != null -> postArriveEvent(event.creature, movedToNeighbor, getDistanceToNeighbor(event.creature.location, movedToNeighbor.location), event.silent)
            NO_VECTOR.getDistance(event.destination) > LOCATION_SIZE -> display("You cannot move that far in that direction.")
            else -> move(event, desiredDistance, actualDistance, actualDestination)
        }
    }

    private fun getActualDistanceMoved(event: MoveEvent, desiredDistance: Int): Int {
        val abilityToMove = event.creature.soul.getCurrent(STAMINA) * Distances.HUMAN_LENGTH
        return if (event.staminaScalar != 0f) {
            min(abilityToMove, desiredDistance)
        } else {
            desiredDistance
        }
    }

    private fun getMovedToNeighbor(locationNode: LocationNode, destination: Vector): LocationPoint? {
        return locationNode.getNeighborConnections()
                .filter { destination.isFurtherAlongSameDirectionThan(it.vector) }
                .minByOrNull { destination.getDistance(it.vector) }
                ?.destination
    }

    private fun move(event: MoveEvent, desiredDistance: Int, actualDistance: Int, actualDestination: Vector) {
        event.creature.position = actualDestination
        displayMovement(event, desiredDistance, actualDistance, actualDestination)
        if (event.staminaScalar != 0f) {
            EventManager.postEvent(StatChangeEvent(event.creature, "moving", STAMINA, (-actualDistance * event.staminaScalar).roundToInt() / Distances.HUMAN_LENGTH, true))
        }
    }

    private fun displayMovement(event: MoveEvent, desiredDistance: Int, actualDistance: Int, actualDestination: Vector) {
        if (!event.silent) {
            val destinationTarget = event.creature.location.getLocation().getTargets(event.creature).firstOrNull { it.position == actualDestination }
            val destinationString = destinationTarget?.getDisplayName() ?: actualDestination.toString()

            val youMove = StringFormatter.format(event.creature.isPlayer(), "You move", "${event.creature} moves")
            val isAre = getIsAre(event.creature)

            if (desiredDistance == actualDistance) {
                display("$youMove from ${event.source} to $destinationString.")
            } else {
                display("$youMove $actualDistance towards ${event.destination} and $isAre now at ${destinationString}.")
            }
        }
    }

}