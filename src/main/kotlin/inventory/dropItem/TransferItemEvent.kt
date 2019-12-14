package inventory.dropItem

import core.events.Event
import core.target.Target

class TransferItemEvent(val mover: Target, val item: Target, val source: Target? = null, val destination: Target? = null, val silent: Boolean = false) : Event {
    init {
        if (source == null && destination == null) {
            throw IllegalArgumentException("Source and Destination cannot both be null!")
        }
    }

    override fun gameTicks(): Int {
        return if (source?.isPlayer() == true) {
            1
        } else {
            0
        }
    }
}