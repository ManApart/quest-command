package core.thing.item

import core.events.EventListener
import core.events.EventManager
import core.history.displayGlobal
import core.properties.COUNT
import traveling.position.NO_VECTOR
import traveling.scope.spawn.ItemSpawnedEvent
import traveling.scope.spawn.SpawnItemEvent

class ItemSpawner : EventListener<SpawnItemEvent>() {
    override suspend fun complete(event: SpawnItemEvent) {
        if (ItemManager.itemExists(event.itemName)) {
            val item = ItemManager.getItem(event.itemName)
            item.properties.values.put(COUNT, event.count)
            if (event.position != NO_VECTOR) {
                item.position = event.position
            }
            EventManager.postEvent(ItemSpawnedEvent(item, event.thing, event.thingLocation ?: event.thing!!.location))
        } else {
            displayGlobal("Could not spawn ${event.itemName} because it could not be found.")
        }
    }
}