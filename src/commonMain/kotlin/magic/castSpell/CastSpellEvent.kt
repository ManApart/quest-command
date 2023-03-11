package magic.castSpell

import core.events.TemporalEvent
import core.thing.Thing
import magic.spells.Spell
import traveling.position.ThingAim

class CastSpellEvent(override val source: Thing, val thing: ThingAim, val spell: Spell, override var timeLeft: Int = spell.castTime) : TemporalEvent {
    override fun gameTicks(): Int = 1
}