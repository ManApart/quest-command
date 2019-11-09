package combat.attack

import combat.battle.Battle
import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.HEALTH
import system.EventManager

class StartAttack : EventListener<StartAttackEvent>() {
    override fun execute(event: StartAttackEvent) {
        when {
            event.source == event.target.target -> EventManager.postEvent(event.getActionEvent())
            event.target.target.soul.hasStat(HEALTH) && GameState.battle == null -> {
                GameState.battle = Battle(listOf(event.source, event.target.target))
                GameState.battle?.start()
                GameState.battle?.addAction(event.source, event)
            }
            event.target.target.soul.hasStat(HEALTH) -> GameState.battle?.addAction(event.source, event)
        }
    }

}