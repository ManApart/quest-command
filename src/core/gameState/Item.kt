package core.gameState

import core.utility.max

class Item(override val name: String, override val description: String = "", val weight: Int = 0, equipSlots: List<List<String>> = listOf(), override val properties: Properties = Properties()) : Target {
 val equipSlots = equipSlots.map { Slot(it) }
    override fun toString(): String {
        return name
    }

    fun copy(): Item {
        return Item(name, description, weight, equipSlots.map { it.bodyParts.map { it } }, properties)
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
        return equipSlots.firstOrNull { it.contains(bodyPart) && body.canEquip(it) }
    }

    fun getDamage() : Int {
        val chop = properties.values.getInt("chopDamage", 1)
        val stab = properties.values.getInt("stabDamage", 1)
        val slash = properties.values.getInt("slashDamage", 1)
        return max(chop, stab, slash)
    }

}