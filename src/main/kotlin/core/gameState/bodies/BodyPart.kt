package core.gameState.bodies

import core.gameState.Item
import core.history.display
import core.utility.Named

class BodyPart(override val name: String, slots: List<String> = listOf()) :Named {
    constructor(base: BodyPart) : this(base.name, base.equippedItems.keys.toList())

    private var equippedItems: MutableMap<String, Item?> = slots.map { it.toLowerCase() to null }.toMap().toMutableMap()

    override fun toString(): String {
        return name
    }

    fun getEquippedItem(slot: String) : Item? {
        return equippedItems[slot.toLowerCase()]
    }

    fun getEquippedItems() : List<Item> {
        return equippedItems.values.filterNotNull()
    }

    fun getEquippedWeapon() : Item? {
        return equippedItems.values.firstOrNull { it?.properties?.tags?.has("Weapon") ?: false }
    }

    fun equipItem(slot: String, item: Item) {
        if (!equippedItems.containsKey(slot.toLowerCase())){
            display("Couldn't equip $item to $slot of body part $name. This should never happen!")
        } else {
            equippedItems[slot.toLowerCase()] = item
        }
    }

    fun unEquip(item: Item) {
        equippedItems.keys.forEach {
            if (equippedItems[it] == item){
                equippedItems[it] = null
            }
        }
    }

}