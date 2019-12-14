package combat.approach

import core.events.EventListener
import core.GameState

class StartMove : EventListener<StartMoveEvent>() {
    override fun execute(event: StartMoveEvent) {
        GameState.battle?.addAction(event.source, event)
    }
}