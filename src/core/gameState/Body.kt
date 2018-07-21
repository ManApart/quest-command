package core.gameState

import core.utility.NameSearchableList

class Body(val name: String = "None", parts: List<String> = listOf()) {
    private val parts = NameSearchableList(parts.map { BodyPart(it) })

    fun copy(): Body {
        return Body(name, parts.map { it.name })
    }

    fun hasPart(part: String): Boolean {
        return parts.exists(part)
    }

    fun getEquippedItems(): NameSearchableList<Item> {
        val items = NameSearchableList<Item>()
        parts.forEach {
            val item = it.equippedItem
            if (item != null && !items.contains(item)) {
                items.add(item)
            }
        }

        return items
    }

    fun getEquippedItemAt(part: String): Item? {
        return getPart(part).equippedItem
    }

    fun getPart(part: String): BodyPart {
        return parts.get(part)
    }

    fun getEquippablePart(part: String, item: Item): BodyPart? {
        return parts.getAll(part).firstOrNull { item.findSlot(this, it.name.toLowerCase()) != null }
    }

    private fun getPartsEquippedWith(item: Item): List<BodyPart> {
        return parts.filter { it.equippedItem == item }
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

    private fun getDefaultSlot(item: Item): Slot {
        return item.equipSlots.firstOrNull { canEquip(it) && it.isEmpty(this) }
                ?: item.equipSlots.first { canEquip(it) }
    }

    fun equip(item: Item, slot: Slot) {
        if (canEquip(slot)) {
            unEquip(item)
            slot.bodyParts.forEach {
                unEquip(it)
            }
            slot.bodyParts.forEach {
                getPart(it).equippedItem = item
            }
        } else {
            println("Can't equip ${item.name} to ${slot.description}")
        }
    }

    fun unEquip(bodyPart: String) {
        val equippedItem = getPart(bodyPart).equippedItem
        if (equippedItem != null) {
            unEquip(equippedItem)
        }
    }

    fun unEquip(item: Item) {
        getPartsEquippedWith(item).forEach {
            it.equippedItem = null
        }
    }

}