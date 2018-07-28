package inventory.pickupItem

import core.events.EventListener

class ItemPickedUp : EventListener<ItemPickedUpEvent>() {
    override fun execute(event: ItemPickedUpEvent) {
        println("${event.source.name} picked up ${event.item}")
    }
}