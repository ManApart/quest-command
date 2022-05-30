package inventory.putItem

import core.events.Event
import core.thing.Thing

class TransferItemEvent(val mover: Thing, val item: Thing, val source: Thing, val destination: Thing, val silent: Boolean = false) : Event {
    override fun gameTicks(): Int {
        return if (mover.isPlayer()) {
            1
        } else {
            0
        }
    }
}