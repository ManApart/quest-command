package combat.attack

import combat.battle.Battle
import core.events.EventListener
import core.gameState.GameState

class StartAttack : EventListener<StartAttackEvent>() {
    override fun execute(event: StartAttackEvent) {
        if (GameState.battle == null) {
            GameState.battle = Battle(listOf(event.source, event.target.target))
            GameState.battle?.start()
            GameState.battle?.addAction(event.source, event)
        } else {
            GameState.battle?.addAction(event.source, event)
        }
    }


}