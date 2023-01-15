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
import crafting.material.DEFAULT_MATERIAL
import traveling.location.location.Location

suspend fun inventory(name: String = "Inventory") : Inventory {
    return Inventory(name, createInventoryBody(name))
}

suspend fun inventory(name: String, items: List<Thing>) : Inventory {
    return Inventory(name, Body().also { it.layout.rootNode.getLocation().addThings(items) })
}

data class Inventory(val name: String = "Inventory", private val body: Body) {
    //TODO
//    constructor(name: String, items: List<Thing>) : this(name, Body().also { it.layout.rootNode.getLocation().addThings(items) })

    override fun toString(): String {
        return name
    }

    suspend fun exists(item: Thing): Boolean {
        return getItemsNameSearchable().exists(item) || NameSearchableList(getAllItems()).exists(item)
    }

    suspend fun getItem(name: String?): Thing? {
        return if (name == null) {
            null
        } else {
            getAllItems().toNameSearchableList().getOrNull(name) ?: NameSearchableList(getAllItems()).getOrNull(name)
        }
    }

    suspend fun getItems(name: String): List<Thing> {
        return getAllItems().toNameSearchableList().getAll(name)
    }

    /**
     * Return all items of this inventory and any sub-inventory
     */
    suspend fun getAllItems(): List<Thing> {
        val items = getItems()
        return (items + items.flatMap { it.inventory.getAllItems() }).toSet().toList()
    }

    suspend fun getItems(): List<Thing> {
        return body.getParts().flatMap { it.getItems() }.toSet().toList()
    }

    private suspend fun getItemsNameSearchable(): NameSearchableList<Thing> {
        return getItems().toNameSearchableList()
    }

    suspend fun addAllByName(items: List<String>) {
        if (items.isNotEmpty()) {
            addAll(ItemManager.getItems(items))
        }
    }

    suspend fun addAll(items: List<Thing>) {
        items.forEach { add(it) }
    }

    private suspend fun findLocationWith(item: Thing): Location? {
        return body.getParts().firstOrNull { it.getItems().contains(item) }
                ?: body.getParts().flatMap { it.getItems() }.firstNotNullOfOrNull { it.inventory.findLocationWith(item) }
    }

    private suspend fun findLocationThatCanTake(item: Thing): Location? {
        return body.getParts().firstOrNull { it.canHold(item) }
                ?: body.getParts().flatMap { it.getItems() }.firstNotNullOfOrNull { it.inventory.findLocationThatCanTake(item) }
    }

    //Eventually add count
    suspend fun attemptToAdd(item: Thing): Boolean {
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

    suspend fun add(item: Thing) {
        if (!attemptToAdd(item)) {
            body.getRootPart().addThing(item)
        }
    }

    suspend fun remove(item: Thing, count: Int = 1) {
        val location = findLocationWith(item)
        if (location != null) {
            if (item.properties.getCount() > count) {
                item.properties.incCount(-count)
            } else {
                location.removeThing(item)
                body.unEquip(item)
            }
        }
    }

    suspend fun findItemsByProperties(properties: Properties): List<Thing> {
        return getAllItems().filter { it.properties.hasAll(properties) }
    }

    suspend fun getWeight(): Int {
        return getItems().sumOf { it.getWeight() }
    }

}

suspend fun createInventoryBody(name: String = "Inventory", capacity: Int? = null): Body {
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