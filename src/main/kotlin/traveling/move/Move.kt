package traveling.move

import combat.battle.Distances.LOCATION_SIZE
import core.events.EventListener
import core.history.display
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject
import traveling.direction.NO_VECTOR
import traveling.direction.Vector
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint
import traveling.scope.ScopeManager
import traveling.travel.getDistanceToNeighbor
import traveling.travel.postArriveEvent

class Move : EventListener<MoveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: MoveEvent) {
        val movedToNeighbor = getMovedToNeighbor(event.creature.location, event.destination)
        when {
            event.destination.z > 0 -> display("${getSubject(event.creature)} ${getIsAre(event.creature)} unable to move into the air.")
            movedToNeighbor != null -> postArriveEvent(event.creature, movedToNeighbor, getDistanceToNeighbor(event.creature.location, movedToNeighbor.location), event.silent)
            NO_VECTOR.getDistance(event.destination) > LOCATION_SIZE -> display("You cannot move that far in that direction.")
            else -> {
                displayMovement(event)
                event.creature.position = event.destination
            }
        }
    }

    private fun displayMovement(event: MoveEvent) {
        if (!event.silent) {
            val destinationTarget = ScopeManager.getScope(event.creature.location).getTargets(event.creature).firstOrNull { it.position == event.destination }
            val destinationString = destinationTarget?.getDisplayName() ?: event.destination.toString()

            if (event.creature.isPlayer()) {
                display("You move from ${event.source} to $destinationString")
            } else {
                display("${event.creature} moves from ${event.source} to $destinationString")
            }
        }
    }

    private fun getMovedToNeighbor(locationNode: LocationNode, destination: Vector): LocationPoint? {
        return locationNode.getNeighborConnections()
                .filter { destination.isFurtherAlongSameDirectionThan(it.vector) }
                .minBy { destination.getDistance(it.vector) }
                ?.destination
    }



}