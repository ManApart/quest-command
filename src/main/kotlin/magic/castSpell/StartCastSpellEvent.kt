package magic.castSpell

import traveling.position.TargetAim
import core.events.DelayedEvent
import core.events.Event
import core.target.Target
import magic.spells.Spell

class StartCastSpellEvent(override val source: Target, val target: TargetAim, val spell: Spell) : Event, DelayedEvent {
    override var timeLeft = spell.castTime


    override fun getActionEvent(): CastSpellEvent {
        return CastSpellEvent(source, target, spell)
    }


}