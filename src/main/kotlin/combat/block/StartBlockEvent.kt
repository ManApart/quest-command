package combat.block

import combat.battle.BattleAction
import combat.battle.position.TargetDirection
import combat.block.BlockEvent
import core.events.Event
import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.stat.AGILITY

class StartBlockEvent(val source: Target, private val part: BodyPart, private val direction: TargetDirection, timeLeft: Int = -1) : Event, BattleAction {

    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1){
            defaultTimeLeft
        } else {
            //TODO - better calculation for block, take into account the shield etc
            val encumbrance = source.getEncumbrance()
            val agility = Math.max(1, source.soul.getCurrent(AGILITY) - (source.soul.getCurrent(AGILITY) * encumbrance).toInt())

            Math.max(1, 100 / agility)
        }
    }

    override fun getActionEvent(): BlockEvent {
        return BlockEvent(source, part, direction)
    }
}
