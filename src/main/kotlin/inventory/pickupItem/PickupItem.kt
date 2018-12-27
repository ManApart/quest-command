package inventory.pickupItem

import core.events.EventListener
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

class PickupItem : EventListener<PickupItemEvent>() {
    override fun execute(event: PickupItemEvent) {
        if (event.from == null){
            event.source.inventory.add(event.item)
            ScopeManager.getScope(event.source.location).removeTarget(event.item)
            EventManager.postEvent(ItemPickedUpEvent(event.source, event.item, event.silent))
        } else {
            if (event.from.properties.tags.has("Container") && event.from.properties.tags.has("Open")) {
                event.source.inventory.add(event.item)
                event.from.inventory.remove(event.item)
                EventManager.postEvent(ItemPickedUpEvent(event.source, event.item, event.silent))
            } else {
                display("Can't take from ${event.from.name}")
            }
        }

    }
}