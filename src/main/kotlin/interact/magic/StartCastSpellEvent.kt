package interact.magic

import combat.battle.BattleAction
import core.events.Event
import core.gameState.Target

class StartCastSpellEvent(override val source: Target, override val actionTarget: Target, val spell: Spell) : Event, BattleAction {

    override var timeLeft = spell.castTime

    override fun getActionEvent(): CastSpellEvent {
        return CastSpellEvent(source, actionTarget, spell)
    }


}