package combat.approach

import core.events.Event
import core.gameState.Target


class ApproachEvent(val source: Target, val isApproaching: Boolean = true) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}