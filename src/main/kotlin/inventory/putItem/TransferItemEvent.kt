package inventory.putItem

import core.events.Event
import core.target.Target

class TransferItemEvent(val mover: Target, val item: Target, val source: Target, val destination: Target, val silent: Boolean = false) : Event {
    override fun gameTicks(): Int {
        return if (mover.isPlayer()) {
            1
        } else {
            0
        }
    }
}