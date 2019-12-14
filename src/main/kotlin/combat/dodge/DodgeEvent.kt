package combat.dodge

import core.events.Event
import core.target.Target
import traveling.direction.Vector


class DodgeEvent(val source: Target, val direction: Vector) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}