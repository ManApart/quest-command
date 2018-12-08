package inventory.pickupItem

import core.events.EventListener
import core.history.display

class ItemPickedUp : EventListener<ItemPickedUpEvent>() {
    override fun execute(event: ItemPickedUpEvent) {
        if (!event.silent){
            display("${event.source.name} picked up ${event.item}.")
        }
    }
}