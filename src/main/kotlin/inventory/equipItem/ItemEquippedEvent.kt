package inventory.equipItem

import core.body.Slot
import core.events.Event
import core.target.Target

class ItemEquippedEvent(val creature: Target, val item: Target, val slot: Slot) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}