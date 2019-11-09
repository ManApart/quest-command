package interact.magic

import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.Target
import interact.magic.spells.Spell

class StartCastSpellEvent(override val source: Target, override val target: TargetAim, val spell: Spell) : Event, BattleAction {

    override var timeLeft = spell.castTime

    override fun getActionEvent(): CastSpellEvent {
        return CastSpellEvent(source, target, spell)
    }


}