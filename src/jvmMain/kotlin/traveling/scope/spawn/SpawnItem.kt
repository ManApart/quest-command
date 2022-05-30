package traveling.scope.spawn

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.then
import inventory.pickupItem.ItemPickedUpEvent

class SpawnItem : EventListener<ItemSpawnedEvent>() {
    override fun execute(event: ItemSpawnedEvent) {
        if (event.thing == null) {
            val name = (event.item.properties.getCount() > 1).then("${event.item.properties.getCount()}x ${event.item.name}s", event.item.name)
            event.item.location = event.thingLocation
            event.item.location.getLocation().addThing(event.item)
            event.item.display("$name appeared.")
        } else {
            event.thing.inventory.add(event.item)
            EventManager.postEvent(ItemPickedUpEvent(event.thing, event.item, true))
        }
    }
}