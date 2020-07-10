package traveling.approach

import core.events.Event
import core.target.Target
import traveling.direction.Vector


class ApproachEvent(val source: Target, val target: Vector = Vector(), val useStamina: Boolean = true) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}