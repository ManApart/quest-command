package interact.magic

import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.Target

class StartCastSpellEvent(val source: Target, val target: Target, val spell: Spell) : Event, BattleAction {
    override var timeLeft = spell.castTime

    override fun getActionEvent(): CastSpellEvent {
        return CastSpellEvent(source, target, spell)
    }


}