package core.gameState.body

import core.gameState.Item
import core.history.display
import core.utility.NameSearchableList
import java.lang.IllegalArgumentException

class Body(val name: String = "None", parts: List<BodyPart> = listOf()) {
    constructor(base: Body) : this(base.name, base.parts.map { BodyPart(it) })

    private val parts = NameSearchableList(parts)

//    fun hasPart(part: String): Boolean {
//        return parts.exists(part)
//    }

    private fun hasAttachPoint(attachPoint: String): Boolean {
        return parts.any { it.hasAttachPoint(attachPoint) }
    }

    fun getEquippedItems(): NameSearchableList<Item> {
        val items = NameSearchableList<Item>()
        parts.forEach { part ->
            part.getEquippedItems().forEach { item ->
                if (!items.contains(item)) {
                    items.add(item)
                }
            }
        }
        return items
    }

    fun getEquippedItemsAt(attachPoint: String): List<Item> {
        return parts.asSequence().map { it.getEquippedItem(attachPoint) }.filterNotNull().toList()
    }

    fun getPart(part: String): BodyPart {
        return parts.get(part)
    }

    private fun getPartsWithAttachPoint(attachPoint: String): List<BodyPart> {
        return parts.filter { it.hasAttachPoint(attachPoint) }
    }

    private fun getPartsEquippedWith(item: Item): List<BodyPart> {
        return parts.filter { it.getEquippedItems().contains(item) }
    }

    fun canEquip(slot: Slot): Boolean {
        return slot.attachPoints.all { hasAttachPoint(it) }
    }

    fun equip(item: Item) {
        equip(item, getDefaultSlot(item))
    }

    fun getDefaultSlot(item: Item): Slot {
        return item.equipSlots.firstOrNull { canEquip(it) && it.isEmpty(this) }
                ?: item.equipSlots.firstOrNull { canEquip(it) }
                ?: throw IllegalArgumentException("Found no Slot for $item for body $name. This should not happen!")
    }

    fun equip(item: Item, slot: Slot) {
        if (canEquip(slot)) {
            unEquip(item)
            slot.attachPoints.forEach { attachPoint ->
                getEquippedItemsAt(attachPoint).forEach {
                    unEquip(it)
                }
            }
            slot.attachPoints.forEach { attachPoint ->
                getPartsWithAttachPoint(attachPoint).forEach { part ->
                    part.equipItem(attachPoint, item)
                }
            }
        } else {
            display("Can't equip ${item.name} to ${slot.description}")
        }
    }

    fun unEquip(item: Item) {
        getPartsEquippedWith(item).forEach {
            it.unEquip(item)
        }
    }

}