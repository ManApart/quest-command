package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.events.EventListener
import core.gameState.Item
import core.history.display
import core.utility.JsonDirectoryParser

object ItemManager {
    private val items = JsonDirectoryParser.parseDirectory("/data/content/items", ::parseFile)
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
        return items.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun itemExists(name: List<String>): Boolean {
        if (name.isEmpty()) return false

        val fullName = name.joinToString(" ").toLowerCase()
        return items.firstOrNull { fullName.contains(it.name.toLowerCase()) } != null
    }

    fun getItem(name: String): Item {
        return items.first { it.name.toLowerCase() == name.toLowerCase() }.copy()
    }

    fun getItem(name: List<String>): Item {
        val fullName = name.joinToString(" ").toLowerCase()
        return items.first { fullName.contains(it.name.toLowerCase()) }.copy()
    }

    fun parseItems(names: List<String>): List<Item> {
        return names.asSequence().map { getItem(it) }.toList()
    }
}