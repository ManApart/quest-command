package inventory.pickupItem

import core.events.EventListener
import core.history.display
import core.utility.asSubject

class ItemPickedUp : EventListener<ItemPickedUpEvent>() {
    override suspend fun complete(event: ItemPickedUpEvent) {
        if (!event.silent) {
            event.source.display { listener -> "${event.source.asSubject(listener)} picked up ${event.item.name}." }
        }
        if (event.source.canConsume(event)) {
            event.source.consume(event)
        }
    }
}