package inventory

import core.body.Body
import core.properties.CONTAINER
import core.properties.OPEN
import core.properties.Properties
import core.properties.SIZE
import core.thing.Thing
import core.thing.item.ItemManager
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.location.Location


data class Inventory(val name: String = "Inventory", private val body: Body = createInventoryBody(name)) {
    constructor(name: String, items: List<Thing>) : this(name, Body().also { it.layout.rootNode.getLocation().addThings(items) })

    override fun toString(): String {
        return name
    }

    fun exists(item: Thing): Boolean {
        return getItemsNameSearchable().exists(item) || NameSearchableList(getAllItems()).exists(item)
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
        return body.getParts().flatMap { it.getItems() }.toSet().toList()
    }

    private fun getItemsNameSearchable(): NameSearchableList<Thing> {
        return getItems().toNameSearchableList()
    }

    fun addAllByName(items: List<String>) {
        if (items.isNotEmpty()) {
            addAll(ItemManager.getItems(items))
        }
    }

    fun addAll(items: List<Thing>) {
        items.forEach { add(it) }
    }

    private fun findLocationWith(item: Thing): Location? {
        return body.getParts().firstOrNull { it.getItems().contains(item) }
                ?: body.getParts().flatMap { it.getItems() }.mapNotNull { it.inventory.findLocationWith(item) }.firstOrNull()
    }

    private fun findLocationThatCanTake(item: Thing): Location? {
        return body.getParts().firstOrNull { it.canHold(item) }
                ?: body.getParts().flatMap { it.getItems() }.mapNotNull { it.inventory.findLocationThatCanTake(item) }.firstOrNull()
    }

    //Eventually add count
    fun attemptToAdd(item: Thing): Boolean {
        val equipSlot = body.getEmptyEquipSlot(item)
        if (equipSlot != null) {
            body.equip(item, equipSlot)
            return true
        }

        val location = findLocationThatCanTake(item) ?: return false
        val match = location.getItems(item.name).firstOrNull()

        if (match != null && item.isStackable(match)) {
            match.properties.incCount(item.properties.getCount())
        } else {
            location.addThing(item)
        }

        return true
    }

    fun add(item: Thing) {
        if (!attemptToAdd(item)) {
            body.getRootPart().addThing(item)
        }
    }

    fun remove(item: Thing, count: Int = 1) {
        val location = findLocationWith(item)
        if (location != null) {
            if (item.properties.getCount() > count) {
                item.properties.incCount(-count)
            } else {
                location.removeThing(item)
            }
        }
    }

    fun findItemsByProperties(properties: Properties): List<Thing> {
        return getAllItems().filter { it.properties.hasAll(properties) }
    }

    fun getWeight(): Int {
        return getItems().sumOf { it.getWeight() }
    }

}

fun createInventoryBody(name: String = "Inventory", capacity: Int? = null): Body {
    return Body(name).also {
        with(it.getRootPart().properties.tags) {
            add(CONTAINER)
            add(OPEN)
        }
        if (capacity != null) {
            it.getRootPart().properties.values.put(SIZE, capacity)
        }
    }
}