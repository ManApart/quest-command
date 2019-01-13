package core.gameState

import core.utility.NameSearchableList
import system.ItemManager

class Inventory(itemNames: List<String> = listOf()) {
    constructor(base: Inventory) : this(base.getItems().map { it.name })

    private val items = NameSearchableList(itemNames.map { ItemManager.getItem(it) })

    fun exists(item: Item): Boolean {
        return items.exists(item) || NameSearchableList(getAllItems()).exists(item)
    }

    fun getItem(name: String): Item? {
        return items.getOrNull(name) ?: NameSearchableList(getAllItems()).getOrNull(name)
    }

    fun getItems(name: String): List<Item> {
        return NameSearchableList(getAllItems()).getAll(name)
    }

    fun add(item: Item) {
        if (items.exists(item.name)) {
            val match = items.get(item.name)
            if (item.isStackable(match)) {
                match.count += item.count
            } else {
                items.add(item)
            }
        } else {
            items.add(item)
        }
    }

    fun remove(item: Item, count: Int = 1) {
        val inventory = findSubInventoryWithItem(item)
        if (inventory != null){
            if (item.count > count){
                item.count -= count
            } else {
                inventory.items.remove(item)
            }
        }
    }

    /**
     * Return all items of this inventory and any sub-inventory
     */
    fun getAllItems(): List<Item> {
        val items = this.items.toMutableList()
        this.items.forEach {
            if (it.inventory.getItems().isNotEmpty()) {
                items.addAll(it.inventory.getAllItems())
            }
        }
        return items
    }

    fun getItems(): List<Item> {
        return items.toList()
    }

    fun findItemsByProperties(properties: Properties): List<Item> {
        return getAllItems().filter { it.properties.hasAll(properties) }
    }

    fun getWeight(): Int {
        return items.sumBy { it.getWeight() }
    }

    fun hasCapacityFor(item: Item, capacity: Int): Boolean {
        return capacity - getWeight() >= item.getWeight()
    }

    fun findSubInventoryFor(item: Item): List<Item> {
        val inventories = mutableListOf<Item>()
        items.forEach {
            if (it.properties.tags.has("Container")
                    && it.properties.tags.has("Open")
                    && it.canBeHeldByContainerWithProperties(item.properties)) {
                inventories.add(it)
            }
        }
        return inventories
    }

    private fun findSubInventoryWithItem(item: Item): Inventory? {
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