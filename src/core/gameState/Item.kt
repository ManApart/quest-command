package core.gameState

class Item(override val name: String, override val description: String = "", val weight: Int = 0, equipSlots: List<List<String>> = listOf(), override val properties: Properties = Properties()) : Target {
 val equipSlots = equipSlots.map { Slot(it) }
    override fun toString(): String {
        return name
    }

    fun copy(): Item {
        return Item(name, description, weight, equipSlots.map { it.bodyParts }, properties)
    }

    fun canEquipTo(body: Body): Boolean {
        equipSlots.forEach { slot ->
            if (body.canEquip(slot)) {
                return true
            }
        }
        return false
    }

    fun getEquippedSlot(body: Body): Slot {
        return equipSlots.first {it.itemIsEquipped(this, body)}
    }

    fun findSlot(body: Body, bodyPart: String) : Slot? {
        return equipSlots.first { it.contains(bodyPart) && body.canEquip(it) }
    }

}