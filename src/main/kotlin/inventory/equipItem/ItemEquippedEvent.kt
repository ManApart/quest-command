package inventory.equipItem

import core.events.Event
import core.target.Target
import core.body.Slot

class ItemEquippedEvent(val creature: Target, val item: Target, val slot: Slot) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}