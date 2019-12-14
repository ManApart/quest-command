package combat.block

import core.events.EventListener
import core.GameState

class StartBlock : EventListener<StartBlockEvent>() {
    override fun execute(event: StartBlockEvent) {
        GameState.battle?.addAction(event.source, event)
    }
}