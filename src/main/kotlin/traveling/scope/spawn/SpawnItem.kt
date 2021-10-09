package traveling.scope.spawn

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.then
import inventory.pickupItem.ItemPickedUpEvent

class SpawnItem : EventListener<ItemSpawnedEvent>() {
    override fun execute(event: ItemSpawnedEvent) {
        if (event.target == null) {
            val name = (event.item.properties.getCount() > 1).then("${event.item.properties.getCount()}x ${event.item.name}s", event.item.name)
            event.item.display("$name appeared.")
            event.item.location = event.targetLocation
            event.item.location.getLocation().addTarget(event.item)
        } else {
            event.target.inventory.add(event.item)
            EventManager.postEvent(ItemPickedUpEvent(event.target, event.item, true))
        }
    }
}