package combat.dodge

import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.Target
import core.gameState.Vector
import core.gameState.stat.AGILITY
import kotlin.math.max

class StartDodgeEvent(override val source: Target, private val direction: Vector, timeLeft: Int = -1) : Event, BattleAction {
    override val target: TargetAim? = null
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

    override fun getActionEvent(): DodgeEvent {
        return DodgeEvent(source, direction)
    }
}
