package traveling.scope.spawn

import core.events.EventListener
import core.GameState
import core.history.display
import core.utility.StringFormatter
import inventory.pickupItem.ItemPickedUpEvent
import core.events.EventManager

class SpawnItem : EventListener<ItemSpawnedEvent>() {
    override fun execute(event: ItemSpawnedEvent) {
        if (event.target == null) {
            val name = StringFormatter.format(event.item.properties.getCount() > 1, "${event.item.properties.getCount()}x ${event.item.name}s", event.item.name)
            display("$name appeared.")
            event.item.location = event.targetLocation ?: GameState.player.location
            event.item.location.getLocation().addTarget(event.item)
        } else {
            event.target.inventory.add(event.item)
            EventManager.postEvent(ItemPickedUpEvent(event.target, event.item, true))
        }
    }
}