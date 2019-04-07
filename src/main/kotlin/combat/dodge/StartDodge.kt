package combat.dodge

import core.events.EventListener
import core.gameState.GameState

class StartDodge : EventListener<StartDodgeEvent>() {
    override fun execute(event: StartDodgeEvent) {
        GameState.battle?.addAction(event.source, event)
    }
}