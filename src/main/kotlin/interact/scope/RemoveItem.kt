package interact.scope

import core.events.EventListener
import core.history.display
import core.utility.StringFormatter
import inventory.pickupItem.ItemPickedUpEvent
import system.EventManager

class RemoveItem : EventListener<RemoveItemEvent>() {
    override fun execute(event: RemoveItemEvent) {
        event.source.inventory.remove(event.item)
    }
}