package core.target.item

import core.events.EventListener
import traveling.position.NO_VECTOR
import core.history.display
import traveling.scope.spawn.ItemSpawnedEvent
import traveling.scope.spawn.SpawnItemEvent
import core.events.EventManager
import core.properties.COUNT

class ItemSpawner : EventListener<SpawnItemEvent>() {
    override fun execute(event: SpawnItemEvent) {
        if (ItemManager.itemExists(event.itemName)) {
            val item = ItemManager.getItem(event.itemName)
            item.properties.values.put(COUNT, event.count)
            if (event.position != NO_VECTOR) {
                item.position = event.position
            }
            EventManager.postEvent(ItemSpawnedEvent(item, event.target))
        } else {
            display("Could not spawn ${event.itemName} because it could not be found.")
        }
    }
}