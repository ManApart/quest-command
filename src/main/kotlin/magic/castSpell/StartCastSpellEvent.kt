package magic.castSpell

import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.target.Target
import magic.spells.Spell

class StartCastSpellEvent(override val source: Target, override val target: TargetAim, val spell: Spell) : Event, BattleAction {

    override var timeLeft = spell.castTime

    override fun getActionEvent(): CastSpellEvent {
        return CastSpellEvent(source, target, spell)
    }


}