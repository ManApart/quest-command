package interact.magic

import combat.battle.Battle
import core.events.EventListener
import core.gameState.GameState
import system.EventManager

class StartCastSpell : EventListener<StartCastSpellEvent>() {
    override fun execute(event: StartCastSpellEvent) {
        if (event.spell.isHostile) {
            if (GameState.battle == null) {
                GameState.battle = Battle(listOf(event.source, event.source))
                //TODO - this should be pulled out into battle.start or something
                GameState.player.canRest = false
                GameState.player.canTravel = false
                GameState.battle?.addAction(event.source, event)
            } else {
                GameState.battle?.addAction(event.source, event)
            }
        } else {
            EventManager.postEvent(event.getActionEvent())
        }
    }


}