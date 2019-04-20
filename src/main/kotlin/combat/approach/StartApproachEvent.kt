package combat.approach

import combat.battle.BattleAction
import core.events.Event
import core.gameState.Target
import core.gameState.stat.AGILITY

class StartApproachEvent(val source: Target, private val isApproaching: Boolean = true, timeLeft: Int = -1) : Event, BattleAction {

    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1){
            defaultTimeLeft
        } else {
            val encumbrance = source.getEncumbrance()
            val agility = Math.max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())

            Math.max(1, 100 / agility)
        }
    }

    override fun getActionEvent(): ApproachEvent {
        return ApproachEvent(source, isApproaching)
    }
}
