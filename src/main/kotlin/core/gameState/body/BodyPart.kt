package core.gameState.body

import combat.battle.position.HitLevel
import core.gameState.Target
import core.history.display
import core.utility.Named

//TODO - can body part be replaced with location, slots be sublocations, and equipped items be items in those locations?
class BodyPart(override val name: String, slots: List<String> = listOf()) : Named {

    constructor(base: BodyPart) : this(base.name, base.equippedItems.keys.toList())

    private var equippedItems: MutableMap<String, Target?> = slots.map { it.toLowerCase() to null }.toMap().toMutableMap()

    override fun toString(): String {
        return name
    }

    fun getAttachPoints(): List<String> {
        return equippedItems.keys.toList()
    }

    fun hasAttachPoint(attachPoint: String): Boolean {
        return equippedItems.map { it.key.toLowerCase() }.contains(attachPoint.toLowerCase())
    }

    fun getEquippedItem(slot: String): Target? {
        return equippedItems[slot.toLowerCase()]
    }

    fun getEquippedItems(): List<Target> {
        return equippedItems.values.filterNotNull()
    }

    fun getEquippedWeapon(): Target? {
        return equippedItems.values.firstOrNull { it?.properties?.tags?.has("Weapon") ?: false }
    }

    fun equipItem(attachPoint: String, item: Target) {
        if (!equippedItems.containsKey(attachPoint.toLowerCase())) {
            display("Couldn't equip $item to $attachPoint of body part $name. This should never happen!")
        } else {
            equippedItems[attachPoint.toLowerCase()] = item
        }
    }

    fun unEquip(item: Target) {
        equippedItems.keys.forEach {
            if (equippedItems[it] == item) {
                equippedItems[it] = null
            }
        }
    }

}