package magic.castSpell

import core.events.Event
import core.thing.Thing
import magic.spells.Spell
import traveling.position.ThingAim

class CastSpellEvent(val source: Thing, val thing: ThingAim, val spell: Spell) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}