package traveling.move

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.clamp
import core.utility.isAre
import core.utility.then
import explore.listen.addSoundEffect
import status.stat.SNEAK
import status.stat.STAMINA
import status.stat.getMaxPossibleMovement
import status.stat.getStaminaCost
import status.statChanged.StatChangeEvent
import traveling.direction.Direction
import traveling.location.Connection
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector
import traveling.travel.postArriveEvent
import kotlin.math.max
import kotlin.math.min

class Move : EventListener<MoveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override suspend fun complete(event: MoveEvent) {
        val desiredDistance = event.sourcePosition.getDistance(event.destination)
        val actualDistance = getActualDistanceMoved(event, desiredDistance)
        val attainableDestination = event.sourcePosition.getVectorInDirection(event.destination, actualDistance)
        val connection = getMovedToConnection(event.source.location, event.source.position, attainableDestination)
        val movedToNeighbor = connection?.destination
        val actualDestination = event.source.position.getNearest(attainableDestination, connection?.source?.vector ?: attainableDestination)
        val vector = actualDestination - event.source.position
        val stamina = event.source.soul.getCurrent(STAMINA)
        val staminaRequired = vector.getDistance() / 10
        val boundedDestination = event.source.currentLocation().bounds.trim(attainableDestination)

        when {
            actualDestination.z > 0 -> event.source.display { "${event.source.asSubject(it)} ${event.source.isAre(it)} unable to move into the air." }
            stamina == 0 -> event.source.display { "${event.source.asSubject(it)} ${event.source.isAre(it)} too tired to move." }
            movedToNeighbor != null -> postArriveEvent(event.source, movedToNeighbor, staminaRequired, event.silent)
            event.source.position == boundedDestination -> event.source.displayToMe("You cannot move that far in that direction.")
            else -> move(event, desiredDistance, actualDistance, boundedDestination)
        }
    }

    private fun getActualDistanceMoved(event: MoveEvent, desiredDistance: Int): Int {
        val abilityToMove = event.source.getMaxPossibleMovement(event.staminaScalar)
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
                it.vector.direction != Direction.NONE && destination.isFurtherAlongSameDirectionThan(it.source.vector) && it.vector.isFurtherAlongSameDirectionThan(destination)
            }
            .minByOrNull { destination.getDistance(it.source.vector) }
    }

    private suspend fun move(event: MoveEvent, desiredDistance: Int, actualDistance: Int, actualDestination: Vector) {
        event.source.position = actualDestination
        //TODO - make materially based description + possibly sound level?
        val soundLevel = (max(10, (event.source.getEncumbrance() * 100).toInt()) - event.source.soul.getCurrent(SNEAK)).clamp(0, 20)
        event.source.addSoundEffect("Moving", "the sound of footfalls", soundLevel)
        displayMovement(event, desiredDistance, actualDistance, actualDestination)
        if (event.staminaScalar != 0f) {
            EventManager.postEvent(StatChangeEvent(event.source, "moving", STAMINA, -getStaminaCost(actualDistance, event.staminaScalar), true))
        }
    }

    private suspend fun displayMovement(event: MoveEvent, desiredDistance: Int, actualDistance: Int, actualDestination: Vector) {
        if (!event.silent) {
            val location = event.source.location.getLocation()
            val things = location.getThings(event.source).filter { it != event.source }
            val destinationThing = things.firstOrNull { it.position == actualDestination }
            val startThing = things.firstOrNull { it.position == event.sourcePosition }
            val destinationString = destinationThing?.getDisplayName() ?: actualDestination.toString()
            val startString = startThing?.getDisplayName() ?: event.sourcePosition.toString()

            val youMove = event.source.isPlayer().then("move", "moves")

            if (desiredDistance == actualDistance) {
                event.source.display { "${event.source.asSubject(it)} $youMove from $startString to $destinationString." }
            } else {
                event.source.display { "${event.source.asSubject(it)}  $youMove $actualDistance towards ${event.destination} and ${event.source.isAre(it)} now at ${destinationString}." }
            }
        }
    }

}