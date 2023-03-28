package magic.castSpell

import core.events.TemporalEvent
import core.thing.Thing
import magic.spells.Spell
import traveling.position.ThingAim

data class CastSpellEvent(override val creature: Thing, val thing: ThingAim, val spell: Spell, override var timeLeft: Int = spell.castTime) : TemporalEvent