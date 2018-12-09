package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.events.EventListener
import core.gameState.Item
import core.history.display
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

object ItemManager {
    private val items = NameSearchableList(JsonDirectoryParser.parseDirectory("/data/content/items", ::parseFile))
    private fun parseFile(path: String): List<Item> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

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