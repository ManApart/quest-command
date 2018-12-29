package inventory.pickupItem

import core.events.EventListener
import core.gameState.Creature
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

class PickupItem : EventListener<PickupItemEvent>() {
    override fun execute(event: PickupItemEvent) {
        if (event.from == null) {
            pickupItemFromScope(event)
        } else {
            pickupItemFromContainer(event.from, event)
        }
    }

    private fun pickupItemFromScope(event: PickupItemEvent) {
        event.item.properties.values.put("locationDescription", "")
        event.source.inventory.add(event.item)
        ScopeManager.getScope(event.source.location).removeTarget(event.item)
        EventManager.postEvent(ItemPickedUpEvent(event.source, event.item, event.silent))
    }

    private fun pickupItemFromContainer(from: Creature, event: PickupItemEvent) {
        if (from.properties.tags.has("Container") && from.properties.tags.has("Open")) {
            event.item.properties.values.put("locationDescription", "")
            event.source.inventory.add(event.item)
            from.inventory.remove(event.item)
            EventManager.postEvent(ItemPickedUpEvent(event.source, event.item, event.silent))
        } else {
            display("Can't take ${event.item.name} from ${from.name}")
        }
    }

}