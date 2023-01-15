package core.thing.item

import core.DependencyInjector
import core.startupLog
import core.thing.Thing
import core.thing.build
import core.thing.thing
import core.utility.NameSearchableList
import core.utility.lazyM
import traveling.location.location.LocationThing

const val ITEM_TAG = "Item"

object ItemManager {
    private var items by lazyM { loadItems() }

    private fun loadItems(): NameSearchableList<Thing> {
        startupLog("Loading Items.")
        val itemsCollection = DependencyInjector.getImplementation(ItemsCollection::class)
        return itemsCollection.values.build(ITEM_TAG)
    }

    fun reset() {
        items = loadItems()
    }

    fun itemExists(name: String): Boolean {
        return items.exists(name)
    }

    suspend fun getItem(name: String): Thing {
        return thing(name) {
            extends(items.get(name))
        }.build()
    }

    suspend fun getItems(names: List<String>): List<Thing> {
        return names.map { getItem(it) }
    }

    fun getAllItems(): List<Thing> {
        return items.toList()
    }

    suspend fun getItemsFromLocationThings(things: List<LocationThing>): List<Thing> {
        return things.map {
            val item = thing(it.name) {
                extends(items.get(it.name))
                param(it.params)
            }.build()
            if (!it.location.isNullOrBlank()) {
                item.properties.values.put("locationDescription", it.location)
            }
            item.position = it.position
            item
        }
    }

    suspend fun getTaggedItemName(item: Thing): String {
        val orig = getItem(item.name)
        val newTags = item.properties.tags.getAll() - orig.properties.tags.getAll()
        return if (newTags.isNotEmpty()) {
            newTags.joinToString(" ") + " " + item.name
        } else {
            item.name
        }
    }

}