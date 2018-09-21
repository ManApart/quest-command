package inventory.pickupItem

import core.events.EventListener
import interact.ScopeManager
import system.EventManager

class PickupItem : EventListener<PickupItemEvent>() {
    override fun execute(event: PickupItemEvent) {
        event.source.inventory.add(event.item)
        ScopeManager.removeTarget(event.item)
        EventManager.postEvent(ItemPickedUpEvent(event.source, event.item, event.silent))
    }
}