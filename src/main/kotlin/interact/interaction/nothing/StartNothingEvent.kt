package interact.interaction.nothing

import combat.battle.BattleAction
import core.events.Event
import core.gameState.Target

class StartNothingEvent(override val source: Target) : Event, BattleAction {
    override val actionTarget = source
    override var timeLeft = 100

    override fun getActionEvent(): Event {
        return NothingEvent(source)
    }

    override fun gameTicks(): Int {
        return 1
    }
}