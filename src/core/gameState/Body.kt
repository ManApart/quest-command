package core.gameState

class Body(val name: String = "None", parts: List<String> = listOf()) {
    private val parts = parts.map { BodyPart(it) }

    fun copy(): Body {
        return Body(name, parts.map { it.name })
    }

    fun hasPart(part: String): Boolean {
        return parts.firstOrNull { it.name.toLowerCase() == part.toLowerCase() } != null
    }

    fun getEquippedItems() : List<Item> {
        val items = mutableListOf<Item>()
        parts.forEach {
            val item = it.equippedItem
            if (item != null && !items.contains(item)){
                items.add(item)
            }
        }

        return items.toList()
    }

    fun getEquippedItemAt(part: String) : Item? {
        return getPart(part).equippedItem
    }

    private fun getPart(part: String): BodyPart {
        return parts.first { it.name.toLowerCase() == part.toLowerCase() }
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

    private fun getDefaultSlot(item: Item) : Slot {
        return item.equipSlots.firstOrNull { canEquip(it) && it.isEmpty(this) } ?: item.equipSlots.first{ canEquip(it) }
    }

    fun equip(item: Item, slot: Slot) {
        unEquip(item)
        slot.bodyParts.forEach {
            unEquip(it)
        }
        slot.bodyParts.forEach {
            getPart(it).equippedItem = item
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