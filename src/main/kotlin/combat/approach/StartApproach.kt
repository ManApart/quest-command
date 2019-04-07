package combat.approach

import core.events.EventListener
import core.gameState.GameState

class StartApproach : EventListener<StartApproachEvent>() {
    override fun execute(event: StartApproachEvent) {
        GameState.battle?.addAction(event.source, event)
    }
}