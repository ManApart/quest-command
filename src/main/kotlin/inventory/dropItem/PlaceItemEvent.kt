package inventory.dropItem

import core.events.Event
import core.target.Target
import traveling.position.Vector

class PlaceItemEvent(val source: Target, val item: Target, val position: Vector = source.position, val silent: Boolean = false) : Event {

    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}