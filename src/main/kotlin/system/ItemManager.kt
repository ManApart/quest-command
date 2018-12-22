package system

import core.events.EventListener
import core.gameState.Item
import core.history.display

object ItemManager {
    private var parser = DependencyInjector.getImplementation(ItemParser::class.java)
    private var items = parser.loadItems()

    class ItemSpawner : EventListener<SpawnItemEvent>() {
        override fun execute(event: SpawnItemEvent) {
            if (itemExists(event.itemName)) {
                val item = getItem(event.itemName)
                item.count = event.count
                EventManager.postEvent(ItemSpawnedEvent(item, event.target))
            } else {
                display("Could not spawn ${event.itemName} because it could not be found.")
            }
        }
    }

    fun reset() {
        parser = DependencyInjector.getImplementation(ItemParser::class.java)
        items = parser.loadItems()
    }

    fun itemExists(name: String): Boolean {
        return items.exists(name)
    }

    fun itemExists(name: List<String>): Boolean {
       return items.exists(name)
    }

    fun getItem(name: String): Item {
        return Item(items.get(name))
    }

    fun getItem(name: List<String>): Item {
        return Item(items.get(name))
    }

    fun getItems(names: List<String>): List<Item> {
        return items.getAll(names).map { Item(it) }
    }
}