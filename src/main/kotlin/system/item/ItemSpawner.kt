package system.item

import core.events.EventListener
import core.history.display
import interact.scope.spawn.ItemSpawnedEvent
import interact.scope.spawn.SpawnItemEvent
import system.EventManager

class ItemSpawner : EventListener<SpawnItemEvent>() {
    override fun execute(event: SpawnItemEvent) {
        if (ItemManager.itemExists(event.itemName)) {
            val item = ItemManager.getItem(event.itemName)
            item.count = event.count
            EventManager.postEvent(ItemSpawnedEvent(item, event.target))
        } else {
            display("Could not spawn ${event.itemName} because it could not be found.")
        }
    }
}