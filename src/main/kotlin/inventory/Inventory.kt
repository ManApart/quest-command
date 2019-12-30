package inventory

import core.properties.Properties
import core.target.Target
import core.target.item.ItemManager
import core.utility.NameSearchableList

class Inventory(itemNames: List<String> = listOf(), items: List<Target> = listOf()) {
    constructor(base: Inventory) : this(base.getItems().map { it.name })

    private val items = NameSearchableList(items + itemNames.map { ItemManager.getItem(it) })

    fun exists(item: Target): Boolean {
        return items.exists(item) || NameSearchableList(getAllItems()).exists(item)
    }

    fun getItem(name: String): Target? {
        return items.getOrNull(name) ?: NameSearchableList(getAllItems()).getOrNull(name)
    }

    fun getItems(name: String): List<Target> {
        return NameSearchableList(getAllItems()).getAll(name)
    }

    fun addAll(items: List<Target>) {
        items.forEach { add(it) }
    }

    fun add(item: Target) {
        if (items.exists(item.name)) {
            val match = items.get(item.name)
            if (item.isStackable(match)) {
                match.properties.incCount(item.properties.getCount())
            } else {
                items.add(item)
            }
        } else {
            items.add(item)
        }
    }

    fun remove(item: Target, count: Int = 1) {
        val inventory = findSubInventoryWithItem(item)
        if (inventory != null) {
            if (item.properties.getCount() > count) {
                item.properties.incCount(-count)
            } else {
                inventory.items.remove(item)
            }
        }
    }

    /**
     * Return all items of this inventory and any sub-inventory
     */
    fun getAllItems(): List<Target> {
        val items = this.items.toMutableList()
        this.items.forEach {
            if (it.inventory.getItems().isNotEmpty()) {
                items.addAll(it.inventory.getAllItems())
            }
        }
        return items
    }

    fun getItems(): List<Target> {
        return items.toList()
    }

    fun findItemsByProperties(properties: Properties): List<Target> {
        return getAllItems().filter { it.properties.hasAll(properties) }
    }

    fun getWeight(): Int {
        return items.sumBy { it.getWeight() }
    }

    fun hasCapacityFor(item: Target, capacity: Int): Boolean {
        return capacity - getWeight() >= item.getWeight()
    }

    fun findSubInventoryFor(item: Target): List<Target> {
        val inventories = mutableListOf<Target>()
        items.forEach {
            if (it.properties.tags.has("Container")
                    && it.properties.tags.has("Open")
                    && it.properties.canBeHeldByContainerWithProperties(item.properties)) {
                inventories.add(it)
            }
        }
        return inventories
    }

    private fun findSubInventoryWithItem(item: Target): Inventory? {
        if (items.exists(item)) {
            return this
        }
        var inventory: Inventory? = null
        var i = 0
        while (inventory == null && i < items.size) {
            inventory = items[i].inventory.findSubInventoryWithItem(item)
            i++
        }
        return inventory
    }

}