package inventory.dropItem

import core.events.EventListener
import core.history.display

class ItemDropped : EventListener<ItemDroppedEvent>() {
    override fun execute(event: ItemDroppedEvent) {
        if (!event.silent){
            display("${event.source.name} dropped up ${event.item}.")
        }
    }
}