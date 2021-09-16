package core.target.item

import core.DependencyInjector
import core.target.Target
import core.target.build
import core.target.target
import core.utility.NameSearchableList
import traveling.location.location.LocationTarget

const val ITEM_TAG = "Item"

object ItemManager {
    private var items = loadItems()

    private fun loadItems() : NameSearchableList<Target>{
        val itemsCollection = DependencyInjector.getImplementation(ItemsCollection::class)
        return itemsCollection.values.build(ITEM_TAG)
    }

    fun reset() {
        items = loadItems()
    }

    fun itemExists(name: String): Boolean {
        return items.exists(name)
    }

    fun getItem(name: String): Target {
        return target(name){
            extends(items.get(name))
        }.build()
    }

    fun getItems(names: List<String>): List<Target> {
        return names.map { getItem(it) }
    }

    fun getAllItems(): List<Target> {
        return items.toList()
    }

    fun getItemsFromLocationTargets(targets: List<LocationTarget>): List<Target> {
        return targets.map {
            val item = target(it.name){
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

    fun getTaggedItemName(item: Target): String {
        val orig = getItem(item.name)
        val newTags = item.properties.tags.getAll() - orig.properties.tags.getAll()
        return if (newTags.isNotEmpty()) {
            newTags.joinToString(" ") + " " + item.name
        } else {
            item.name
        }
    }

}