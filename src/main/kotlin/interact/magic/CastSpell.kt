package interact.magic

import core.events.EventListener
import core.gameState.GameState
import core.gameState.stat.FOCUS
import core.history.display
import status.effects.AddConditionEvent
import status.statChanged.StatChangeEvent
import system.EventManager
import system.debug.DebugType

class CastSpell : EventListener<CastSpellEvent>() {

    override fun execute(event: CastSpellEvent) {
        if (canCast(event)) {
            event.spell.cast(event)
        }
    }

    private fun canCast(event: CastSpellEvent): Boolean {
        val level = event.source.soul.getCurrent(event.spell.statRequired)
        val focus = event.source.soul.getCurrent(FOCUS)
        return when {
            level < event.spell.levelRequired && !GameState.properties.values.getBoolean(DebugType.LEVEL_REQ.propertyName) -> {
                display("You are too low level to speak this word with this amount of force. ($level/$event.spell.levelRequired)")
                false
            }
            focus < event.spell.cost && !GameState.properties.values.getBoolean(DebugType.STAT_CHANGES.propertyName) -> {
                display("You do not have enough focus to speak this word with this amount of force. ($focus/${event.spell.cost})")
                false
            }
            else -> true
        }
    }

}