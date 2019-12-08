package interact.magic

import combat.battle.Battle
import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.HEALTH
import system.EventManager

class StartCastSpell : EventListener<StartCastSpellEvent>() {
    override fun execute(event: StartCastSpellEvent) {
        when {
            event.source == event.target.target -> EventManager.postEvent(event.getActionEvent())
            !event.spell.isHostile -> EventManager.postEvent(event.getActionEvent())
            event.target.target.soul.hasStat(HEALTH) && GameState.battle == null -> {
                GameState.battle = Battle(listOf(event.source, event.target.target))
                GameState.battle?.start()
                GameState.battle?.addAction(event.source, event)
            }
            event.target.target.soul.hasStat(HEALTH) -> GameState.battle?.addAction(event.source, event)
            else -> EventManager.postEvent(event.getActionEvent())
        }
    }


}