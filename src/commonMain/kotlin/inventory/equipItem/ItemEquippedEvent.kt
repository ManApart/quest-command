package inventory.equipItem

import core.body.Slot
import core.events.Event
import core.thing.Thing

class ItemEquippedEvent(val creature: Thing, val item: Thing, val slot: Slot) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}