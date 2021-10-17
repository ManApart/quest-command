package inventory.pickupItem

import core.events.Event
import core.thing.Thing

class TakeItemEvent(val taker: Thing, val item: Thing, val silent: Boolean = false) : Event {

    override fun gameTicks(): Int {
        return if (taker.isPlayer()) {
            1
        } else {
            0
        }
    }
}