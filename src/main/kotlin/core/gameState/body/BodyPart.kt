package core.gameState.body

import core.gameState.Item
import core.history.display
import core.utility.Named

class BodyPart(override val name: String, slots: List<String> = listOf()) : Named {
    constructor(base: BodyPart) : this(base.name, base.equippedItems.keys.toList())

    private var equippedItems: MutableMap<String, Item?> = slots.map { it.toLowerCase() to null }.toMap().toMutableMap()

    override fun toString(): String {
        return name
    }

    fun getAttachPoints(): List<String> {
        return equippedItems.keys.toList()
    }

    fun hasAttachPoint(attachPoint: String): Boolean {
        return equippedItems.map { it.key.toLowerCase() }.contains(attachPoint.toLowerCase())
    }

    fun getEquippedItem(slot: String): Item? {
        return equippedItems[slot.toLowerCase()]
    }

    fun getEquippedItems(): List<Item> {
        return equippedItems.values.filterNotNull()
    }

    fun getEquippedWeapon(): Item? {
        return equippedItems.values.firstOrNull { it?.properties?.tags?.has("Weapon") ?: false }
    }

    fun equipItem(attachPoint: String, item: Item) {
        if (!equippedItems.containsKey(attachPoint.toLowerCase())) {
            display("Couldn't equip $item to $attachPoint of body part $name. This should never happen!")
        } else {
            equippedItems[attachPoint.toLowerCase()] = item
        }
    }

    fun unEquip(item: Item) {
        equippedItems.keys.forEach {
            if (equippedItems[it] == item) {
                equippedItems[it] = null
            }
        }
    }

}