package combat.battle

import core.events.EventListener
import core.gameState.GameState
import system.gameTick.GameTickEvent

class BattleTick : EventListener<GameTickEvent>() {

    override fun shouldExecute(event: GameTickEvent): Boolean {
        return GameState.battle != null
    }

    override fun execute(event: GameTickEvent) {
        GameState.battle?.takeTurn()
    }
}