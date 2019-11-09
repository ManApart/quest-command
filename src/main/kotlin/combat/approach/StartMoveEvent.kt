package combat.approach

import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.Vector
import core.gameState.stat.AGILITY
import kotlin.math.max

class StartMoveEvent(override val source: Target, private val moveTarget: Vector, timeLeft: Int = -1) : Event, BattleAction {
    override val target: TargetAim? = null
    override var timeLeft = calcTimeLeft(timeLeft)

    private fun calcTimeLeft(defaultTimeLeft: Int): Int {
        return if (defaultTimeLeft != -1) {
            defaultTimeLeft
        } else {
            val agility = getUnencumberedAgility(source)
            val distance = GameState.battle?.getCombatantDistance() ?: 0
            val speed = max(agility - distance, 1)

            max(1, 100 / speed)
        }
    }

    private fun getUnencumberedAgility(target: Target): Int {
        val agility = target.soul.getCurrent(AGILITY)
        val encumbrance = target.getEncumbranceInverted()
        return (agility * encumbrance).toInt()
    }

    override fun getActionEvent(): MoveEvent {
        return MoveEvent(source, moveTarget)
    }
}
