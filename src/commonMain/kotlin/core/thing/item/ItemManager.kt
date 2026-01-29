package core.thing.item

import building.ModManager
import core.DependencyInjector
import core.properties.TagKey.ITEM
import core.startupLog
import core.thing.Thing
import core.thing.build
import core.thing.thing
import core.utility.Backer
import core.utility.NameSearchableList
import traveling.location.location.LocationThing

object ItemManager {
    private val items = Backer(::loadItems)
    suspend fun getItems() = items.get()

    private suspend fun loadItems(): NameSearchableList<Thing> {
        startupLog("Loading Items.")
        val itemsCollection = DependencyInjector.getImplementation(ItemsCollection::class)
        return (itemsCollection.values() + ModManager.items).build(ITEM)
    }

    suspend fun reset() {
        items.reset()
    }

    suspend fun itemExists(name: String): Boolean {
        return getItems().exists(name)
    }

    suspend fun getItem(name: String): Thing {
        return thing(name) {
            extends(getItems().get(name))
        }.build()
    }

    suspend fun getItems(names: List<String>): List<Thing> {
        return names.map { getItem(it) }
    }

    suspend fun getAllItems(): List<Thing> {
        return getItems().toList()
    }

    suspend fun getItemsFromLocationThings(things: List<LocationThing>): List<Thing> {
        return things.map {
            val item = thing(it.name) {
                extends(getItems().get(it.name))
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
        val newTags = item.properties.tags.getAll() - orig.properties.tags.getAll().toSet()
        return if (newTags.isNotEmpty()) {
            newTags.joinToString(" ") + " " + item.name
        } else {
            item.name
        }
    }

}