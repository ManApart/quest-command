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
import traveling.position.NO_VECTOR

enum class FitReason { FITS, NO_CAPACITY, TAG_TOO_SMALL, BODY_TOO_SMALL, CAN_NOT_HOLD }

class Inventory(items: List<Thing> = emptyList()) {
    private val items: MutableList<Thing> = items.toMutableList()

    override fun toString(): String {
        return "${items.size} items"
    }

    override fun hashCode() = items.hashCode()
    override fun equals(other: Any?): Boolean {
        return other is Inventory && items == other.items
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

    suspend fun addAllByName(owner: Thing, items: List<String>) {
        if (items.isNotEmpty()) {
            addAll(owner, ItemManager.getItems(items))
        }
    }

    fun addAll(owner: Thing, items: List<Thing>) {
        items.forEach { add(owner, it) }
    }

    fun add(owner: Thing, item: Thing) {
        if (!attemptToAdd(owner, item)) {
            addStackOrSingle(item, owner.body)
        }
    }

    //Eventually add count of item
    fun attemptToAdd(owner: Thing, item: Thing): Boolean {
        if (hasRoomFor(owner, item)) {
            addStackOrSingle(item, owner.body)
            return true
        }
        return getItems().filter { it.isOpenContainer() }.any { it.attemptToAdd(item) }
    }

    fun getUsedCapacity() = items.size

    fun hasRoomFor(owner: Thing, item: Thing): Boolean {
        return hasRoomForExplained(owner, item) == FitReason.FITS
    }

    fun hasRoomForExplained(owner: Thing, item: Thing): FitReason {
        val tagLarger = owner.properties.tags.isAsLargeOrLargerThan(item.properties.tags)
        return when {
            getUsedCapacity() >= owner.getCapacity() -> FitReason.NO_CAPACITY
            !tagLarger -> FitReason.TAG_TOO_SMALL
            !fits(owner, item) -> FitReason.BODY_TOO_SMALL
            !canHold(owner,item) -> FitReason.CAN_NOT_HOLD
            else -> FitReason.FITS
        }
    }

    private fun canHold(owner: Thing, item: Thing): Boolean {
        return item.properties.canBeHeldByContainerWithProperties(owner.properties)
    }

    private fun fits(owner: Thing, item: Thing): Boolean {
        val ownerD = owner.getDimensions()
        val thingD = item.getDimensions()
        return when {
            ownerD == NO_VECTOR -> true
            thingD == NO_VECTOR -> true
            else -> ownerD.contains(thingD)
        }
    }

    private fun addStackOrSingle(item: Thing, body: Body? = null) {
        val match = items.toNameSearchableList().getOrNull(item.name)
        if (match != null && item.isStackable(match)) {
            match.properties.incCountWithDefault(item.properties.getCount())
            item.inventory.items.forEach { match.inventory.addStackOrSingle(it, match.body) }
        } else {
            items.add(item)
            body?.equipToEmpty(item)
            item.location = NOWHERE_NODE
        }
    }

    fun remove(item: Thing, count: Int = 1, body: Body? = null): Int {
        return if (items.contains(item)) {
            removeStackOrSingle(item, count, body)
        } else {
            items.firstNotNullOfOrNull { it.inventory.remove(item, count, body).takeIf { removed -> removed != 0 } } ?: 0
        }
    }

    private fun removeStackOrSingle(item: Thing, count: Int = 1, body: Body? = null): Int {
        val currentCount = item.properties.getCount()
        val result = currentCount - count

        return if (result < 1) {
            items.remove(item)
            body?.unEquip(item)
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
