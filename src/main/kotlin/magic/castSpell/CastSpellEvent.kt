package magic.castSpell

import traveling.position.TargetAim
import core.events.Event
import core.target.Target
import magic.spells.Spell

class CastSpellEvent(val source: Target, val target: TargetAim, val spell: Spell) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}