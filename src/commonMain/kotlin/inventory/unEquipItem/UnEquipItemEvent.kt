package inventory.unEquipItem

import core.events.Event
import core.thing.Thing

class UnEquipItemEvent(val creature: Thing, val item: Thing) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}