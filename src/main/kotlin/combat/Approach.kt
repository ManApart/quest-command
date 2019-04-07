package combat

import core.events.EventListener
import core.gameState.GameState
import core.history.display

class Approach : EventListener<ApproachEvent>() {

    override fun execute(event: ApproachEvent) {
        if (GameState.battle != null) {
            val currentDistance = GameState.battle!!.targetDistance
            if (event.isApproaching) {
                GameState.battle!!.targetDistance = currentDistance.closer()
            } else {
                GameState.battle!!.targetDistance = currentDistance.farther()
            }
            display("${event.source} moved from ${currentDistance.name} range to ${GameState.battle!!.targetDistance.name} range.")
        }
    }

}