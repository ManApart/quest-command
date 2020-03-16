package combat.block

import combat.battle.BattleAction
import combat.battle.position.TargetAim
import core.events.Event
import core.target.Target
import status.stat.AGILITY
import traveling.location.location.LocationRecipe
import kotlin.math.max

class StartBlockEvent(override val source: Target, private val partThatWillShield: LocationRecipe, private val partThatWillBeShielded: LocationRecipe, timeLeft: Int = -1) : Event, BattleAction {
    override val target: TargetAim? = null
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
