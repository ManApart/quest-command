package magic.castSpell

import core.GameState
import core.events.EventListener
import core.history.displayToMe
import status.stat.Attributes.FOCUS
import status.stat.Attributes.HEALTH
import system.debug.DebugType

class CastSpell : EventListener<CastSpellEvent>() {

    override suspend fun complete(event: CastSpellEvent) {
        if (canCast(event)) {
            event.spell.cast(event)
            if (event.spell.isHostile && event.thing.thing.soul.hasStat(HEALTH) && event.thing.thing != event.creature){
                event.creature.mind.setAggroTarget(event.thing.thing)
                event.thing.thing.mind.setAggroTarget(event.creature)
            }
        }
    }

    //TODO - what if spell is hostile and thing has health?
    private fun canCast(event: CastSpellEvent): Boolean {
        val level = event.creature.soul.getCurrent(event.spell.statRequired)
        val focus = event.creature.soul.getCurrent(FOCUS)
        return when {
            level < event.spell.levelRequired && !GameState.getDebugBoolean(DebugType.LEVEL_REQ) -> {
                event.creature.displayToMe("You are too low level to speak this word with this amount of force. ($level/$event.spell.levelRequired)")
                false
            }
            focus < event.spell.cost && !GameState.getDebugBoolean(DebugType.STAT_CHANGES) -> {
                event.creature.displayToMe("You do not have enough focus to speak this word with this amount of force. ($focus/${event.spell.cost})")
                false
            }
            else -> true
        }
    }

}