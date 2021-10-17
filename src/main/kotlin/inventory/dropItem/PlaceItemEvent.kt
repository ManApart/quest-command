package inventory.dropItem

import core.events.Event
import core.thing.Thing
import traveling.position.Vector

class PlaceItemEvent(val source: Thing, val item: Thing, val position: Vector = source.position, val silent: Boolean = false) : Event {

    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}