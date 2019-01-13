package system.item

import core.gameState.Item
import core.gameState.location.LocationTarget
import system.DependencyInjector

object ItemManager {
    private var parser = DependencyInjector.getImplementation(ItemParser::class.java)
    private var items = parser.loadItems()

    fun reset() {
        parser = DependencyInjector.getImplementation(ItemParser::class.java)
        items = parser.loadItems()
    }

    fun itemExists(name: String): Boolean {
        return items.exists(name)
    }

    fun getItem(name: String): Item {
        return Item(items.get(name))
    }

    fun getItems(names: List<String>): List<Item> {
        return names.map { getItem(it) }
    }

    fun getItemsFromLocationTargets(targets: List<LocationTarget>): List<Item> {
        return targets.map {
            val item = Item(items.get(it.name), it.params)
            if (!it.location.isNullOrBlank()) {
                item.properties.values.put("locationDescription", it.location!!)
            }
            item
        }
    }
}