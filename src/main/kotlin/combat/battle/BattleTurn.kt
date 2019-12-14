package combat.battle

import core.events.EventListener
import core.GameState

class BattleTurn : EventListener<BattleTurnEvent>() {

    override fun execute(event: BattleTurnEvent) {
        GameState.battle?.takeTurn()
    }
}