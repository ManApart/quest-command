package magic.castSpell

import core.GameState
import core.events.EventListener
import core.history.displayToMe
import status.stat.FOCUS
import status.stat.HEALTH
import system.debug.DebugType

class CastSpell : EventListener<CastSpellEvent>() {

    override fun execute(event: CastSpellEvent) {
        if (canCast(event)) {
            event.spell.cast(event)
            if (event.spell.isHostile && event.thing.thing.soul.hasStat(HEALTH) && event.thing.thing != event.source){
                event.source.mind.ai.aggroTarget = event.thing.thing
                event.thing.thing.mind.ai.aggroTarget = event.source
            }
        }
    }

    //TODO - what if spell is hostile and thing has health?
    private fun canCast(event: CastSpellEvent): Boolean {
        val level = event.source.soul.getCurrent(event.spell.statRequired)
        val focus = event.source.soul.getCurrent(FOCUS)
        return when {
            level < event.spell.levelRequired && !GameState.getDebugBoolean(DebugType.LEVEL_REQ) -> {
                event.source.displayToMe("You are too low level to speak this word with this amount of force. ($level/$event.spell.levelRequired)")
                false
            }
            focus < event.spell.cost && !GameState.getDebugBoolean(DebugType.STAT_CHANGES) -> {
                event.source.displayToMe("You do not have enough focus to speak this word with this amount of force. ($focus/${event.spell.cost})")
                false
            }
            else -> true
        }
    }

}