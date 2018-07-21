package inventory.pickupItem

import core.events.EventListener
import interact.ScopeManager

class PickupItem : EventListener<PickupItemEvent>() {
    override fun execute(event: PickupItemEvent) {
        println("${event.source} picked up ${event.item}")
        event.source.inventory.items.add(event.item)
        ScopeManager.removeTarget(event.item)
    }
}