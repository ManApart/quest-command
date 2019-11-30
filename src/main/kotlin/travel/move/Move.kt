package travel.move

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Target
import core.gameState.Vector
import core.history.display
import core.history.displayIf
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject
import interact.scope.ScopeManager
import travel.ArriveEvent

class Move : EventListener<MoveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: MoveEvent) {
        if (event.destination.z > 0) {
            display("${getSubject(event.creature)} ${getIsAre(event.creature)} unable to move into the air.")
        } else {
            displayMovement(event)
            event.creature.position = event.destination
        }
    }

    private fun displayMovement(event: MoveEvent) {
        if (!event.silent) {
            if (event.creature.isPlayer()) {
                display("You move from ${event.source} to ${event.destination}")
            } else {
                display("${event.creature} moves from ${event.source} to ${event.destination}")
            }
        }
    }

}