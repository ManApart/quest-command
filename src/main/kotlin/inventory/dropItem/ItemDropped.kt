package inventory.dropItem

import core.events.EventListener
import core.history.display

class ItemDropped : EventListener<ItemDroppedEvent>() {
    override fun execute(event: ItemDroppedEvent) {
        if (!event.silent){
            event.source.display("${event.source.name} dropped ${event.item.name}.")
        }
    }
}