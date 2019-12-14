package use.interaction.nothing

import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.target.Target

class StartNothingEvent(override val source: Target, private val hoursWaited: Int = 0) : Event, BattleAction {
    override val target = TargetAim(source)
    override var timeLeft = 100

    override fun getActionEvent(): Event {
        return NothingEvent(source, hoursWaited)
    }

    override fun gameTicks(): Int {
        return 1
    }
}