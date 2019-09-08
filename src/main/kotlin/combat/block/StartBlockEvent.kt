package combat.block

import combat.battle.BattleAction
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.stat.AGILITY
import kotlin.math.max

class StartBlockEvent(override val source: Target, private val partThatWillShield: BodyPart, val partThatWillBeShielded: BodyPart, timeLeft: Int = -1) : Event, BattleAction {
    override val actionTarget: Target? = null
    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1){
            defaultTimeLeft
        } else {
            //TODO - better calculation for block, take into account the shield etc
            val encumbrance = source.getEncumbrance()
            val agility = max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())

            max(1, 100 / agility)
        }
    }

    override fun getActionEvent(): BlockEvent {
        return BlockEvent(source, partThatWillShield, partThatWillBeShielded)
    }
}
