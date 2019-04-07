package combat.approach

import core.events.Event
import core.gameState.Creature
import core.gameState.isPlayer

class ApproachEvent(val source: Creature, val isApproaching: Boolean = true) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}