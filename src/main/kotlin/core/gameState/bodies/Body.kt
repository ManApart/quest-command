package core.gameState.bodies

import core.gameState.Item
import core.history.display
import core.utility.NameSearchableList
import java.lang.IllegalArgumentException

class Body(val name: String = "None", parts: List<BodyPart> = listOf()) {
    constructor(base: Body) : this(base.name, base.parts.map { BodyPart(it) })

    private val parts = NameSearchableList(parts)

    fun hasPart(part: String): Boolean {
        return parts.exists(part)
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

    fun getEquippedItemsAt(part: String): List<Item> {
        return getPart(part).getEquippedItems()
    }

    fun getPart(part: String): BodyPart {
        return parts.get(part)
    }

    fun getEquippablePart(part: String, item: Item): BodyPart? {
        return parts.getAll(part).firstOrNull { item.findSlot(this, it.name.toLowerCase()) != null }
    }

    private fun getPartsEquippedWith(item: Item): List<BodyPart> {
        return parts.filter { it.getEquippedItems().contains(item) }
    }

    fun canEquip(slot: Slot): Boolean {
        slot.bodyParts.forEach { part ->
            if (!hasPart(part)) {
                return false
            }
        }
        return true
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
            slot.bodyParts.forEach {
                unEquip(it)
            }
            slot.bodyParts.forEach {
                getPart(it).equipItem(it, item)
            }
        } else {
            display("Can't equip ${item.name} to ${slot.description}")
        }
    }

    private fun unEquip(bodyPart: String) {
        getPart(bodyPart).getEquippedItems().forEach {
            unEquip(it)
        }
    }

    fun unEquip(item: Item) {
        getPartsEquippedWith(item).forEach {
            it.unEquip(item)
        }
    }

}