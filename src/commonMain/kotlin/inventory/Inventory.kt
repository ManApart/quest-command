package inventory

import core.body.Body
import core.body.body
import core.properties.Properties
import core.properties.TagStrings.CONTAINER
import core.properties.TagStrings.OPEN
import core.thing.Thing
import core.thing.item.ItemManager
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.network.NOWHERE_NODE

class Inventory(items: List<Thing> = emptyList()) {
    private val items: MutableList<Thing> = items.toMutableList()

    override fun toString(): String {
        return "${items.size} items"
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

    suspend fun addAllByName(items: List<String>, body: Body) {
        if (items.isNotEmpty()) {
            addAll(ItemManager.getItems(items), 0, body)
        }
    }

    fun addAll(items: List<Thing>, capacity: Int, body: Body) {
        items.forEach { add(it, capacity, body) }
    }

    fun add(item: Thing, capacity: Int, body: Body) {
        if (!attemptToAdd(item, capacity, body)) {
            addStackOrSingle(item, body)
        }
    }

    //Eventually add count of item
    fun attemptToAdd(item: Thing, capacity: Int, body: Body): Boolean {
        if (rootHasRoomFor(item, capacity)) {
            addStackOrSingle(item, body)
            return true
        }
        return getItems().filter { it.isOpenContainer() }.any { it.attemptToAdd(item) }
    }

    private fun rootHasRoomFor(item: Thing, capacity: Int): Boolean {
        val used = getWeight()
        return item.getWeight() + used <= capacity
    }

    private fun addStackOrSingle(item: Thing, body: Body) {
        val match = items.toNameSearchableList().getOrNull(item.name)
        if (match != null && item.isStackable(match)) {
            match.properties.incCount(item.properties.getCount())
        } else {
            items.add(item)
            body.equipToEmpty(item)
            item.location = NOWHERE_NODE
        }
    }

    fun remove(item: Thing, count: Int = 1, body: Body): Int {
        return if (items.contains(item)) {
            removeStackOrSingle(item, count, body)
        } else {
            items.firstNotNullOfOrNull { it.inventory.remove(item, count, body).takeIf { removed -> removed != 0 } } ?: 0
        }
    }

    private fun removeStackOrSingle(item: Thing, count: Int, body: Body): Int {
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
