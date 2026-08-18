package inventory

import core.body.Body
import core.properties.Properties
import core.properties.TagStrings.CONTAINER
import core.properties.TagStrings.OPEN
import core.thing.Thing
import core.thing.item.ItemManager
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.network.NOWHERE_NODE

fun inventory(name: String = "Inventory"): Inventory {
    return Inventory(name, Body(name))
}

fun inventory(name: String, items: List<Thing>): Inventory {
    return Inventory(name, Body(), items.toMutableList())
}

data class Inventory(val name: String = "Inventory", private val body: Body, private val items: MutableList<Thing> = mutableListOf()) {
    constructor(name: String, items: List<Thing>) : this(name, Body(), items.toMutableList())

    override fun toString(): String {
        return name
    }

    fun exists(item: Thing): Boolean {
        return items.contains(item) || getAllItems().contains(item)
    }

    fun getItem(name: String?): Thing? {
        return if (name == null) {
            null
        } else {
            getAllItems().toNameSearchableList().getOrNull(name) ?: NameSearchableList(getAllItems()).getOrNull(name)
        }
    }

    fun getItems(name: String): List<Thing> {
        return getAllItems().toNameSearchableList().getAll(name)
    }

    /**
     * Return all items of this inventory and any sub-inventory
     */
    fun getAllItems(): List<Thing> {
        val items = getItems()
        return (items + items.flatMap { it.inventory.getAllItems() }).toSet().toList()
    }

    fun getItems(): List<Thing> {
        return items.toList()
    }

    suspend fun addAllByName(items: List<String>) {
        if (items.isNotEmpty()) {
            addAll(ItemManager.getItems(items), 0)
        }
    }

    fun addAll(items: List<Thing>, capacity: Int) {
        items.forEach { add(it, capacity) }
    }

    fun add(item: Thing, capacity: Int) {
        if (!attemptToAdd(item, capacity)) {
            addStackOrSingle(item)
        }
    }

    //Eventually add count of item
    fun attemptToAdd(item: Thing, capacity: Int): Boolean {
        if (rootHasRoomFor(item, capacity)) {
            addStackOrSingle(item)
            return true
        }
        return getItems().filter { it.isOpenContainer() }.any { it.attemptToAdd(item) }
    }

    private fun rootHasRoomFor(item: Thing, capacity: Int): Boolean {
        val used = getWeight()
        return item.getWeight() + used <= capacity
    }

    private fun addStackOrSingle(item: Thing) {
        val match = items.toNameSearchableList().getOrNull(item.name)
        if (match != null && item.isStackable(match)) {
            match.properties.incCount(item.properties.getCount())
        } else {
            items.add(item)
            body.equipToEmpty(item)
            item.location = NOWHERE_NODE
        }
    }

    fun remove(item: Thing, count: Int = 1): Int {
        return if (items.contains(item)) {
            removeStackOrSingle(item, count)
        } else {
            items.firstNotNullOfOrNull { it.inventory.remove(item, count).takeIf { removed -> removed != 0 } } ?: 0
        }
    }

    private fun removeStackOrSingle(item: Thing, count: Int): Int {
        val currentCount = item.properties.getCount()
        val result = currentCount - count

        return if (result < 1) {
            items.remove(item)
            body.unEquip(item)
            currentCount
        } else {
            item.properties.incCount(-count)
            count
        }
    }

    fun findItemsByProperties(properties: Properties): List<Thing> {
        return getAllItems().filter { it.properties.hasAll(properties) }
    }

    fun getWeight(): Int {
        return getItems().sumOf { it.getWeight() }
    }

}


private fun Thing.isOpenContainer() = with(properties.tags) {
    has(CONTAINER) && has(OPEN)
}

//
//suspend fun createInventoryBody(name: String = "Inventory"): Body {
//    return Body(name).also {
//        with(it.getRootPart().properties.tags) {
//            add(CONTAINER)
//            add(OPEN)
//        }
//        if (capacity != null) {
//            it.getRootPart().properties.values.put(SIZE, capacity)
//        }
//    }
//}
