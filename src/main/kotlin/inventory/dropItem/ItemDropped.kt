package inventory.dropItem

import core.events.EventListener
import core.history.display
import core.utility.asSubject

class ItemDropped : EventListener<ItemDroppedEvent>() {
    override fun execute(event: ItemDroppedEvent) {
        if (!event.silent) {
            event.source.display { listener -> "${event.source.asSubject(listener)} dropped ${event.item.name}." }
        }
    }
}