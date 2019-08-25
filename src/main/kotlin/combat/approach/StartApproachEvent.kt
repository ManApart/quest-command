package combat.approach

import combat.battle.BattleAction
import core.events.Event
import core.gameState.Target
import core.gameState.stat.AGILITY
import kotlin.math.max

class StartApproachEvent(val source: Target, private val amount: Int, private val isApproaching: Boolean = true, timeLeft: Int = -1) : Event, BattleAction {

    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1){
            defaultTimeLeft
        } else {
            val encumbrance = source.getEncumbrance()
            val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())

            max(1, 100 / agility)
        }
    }

    override fun getActionEvent(): ApproachEvent {
        return ApproachEvent(source, amount, isApproaching)
    }
}
