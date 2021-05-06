package inventory.pickupItem

import core.events.Event
import core.target.Target

class TakeItemEvent(val taker: Target, val item: Target, val silent: Boolean = false) : Event {

    override fun gameTicks(): Int {
        return if (taker.isPlayer()) {
            1
        } else {
            0
        }
    }
}