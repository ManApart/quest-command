package magic.castSpell

import core.events.DelayedEvent
import core.events.Event
import core.thing.Thing
import magic.spells.Spell
import traveling.position.ThingAim

class StartCastSpellEvent(override val source: Thing, val thing: ThingAim, val spell: Spell) : Event, DelayedEvent {
    override var timeLeft = spell.castTime


    override fun getActionEvent(): CastSpellEvent {
        return CastSpellEvent(source, thing, spell)
    }


}