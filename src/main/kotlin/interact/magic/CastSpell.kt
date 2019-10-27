package interact.magic

import core.events.EventListener
import core.gameState.stat.FOCUS
import core.history.display
import status.effects.AddConditionEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class CastSpell : EventListener<CastSpellEvent>() {

    override fun execute(event: CastSpellEvent) {
        if (canCast(event)){
            event.spell.cast(event)
        }
    }

    private fun canCast(event: CastSpellEvent): Boolean {
        val level = event.source.soul.getCurrent(event.spell.statRequired)
        val focus = event.source.soul.getCurrent(FOCUS)
        return when {
            level < event.spell.levelRequired -> {
                display("You are too low level to speak this word with this amount of force. ($level/$event.spell.levelRequired)")
                false
            }
            focus < event.spell.cost -> {
                display("You do not have enough focus to speak this word with this amount of force. ($focus/${event.spell.cost})")
                false
            }
            else -> true
        }
    }

}