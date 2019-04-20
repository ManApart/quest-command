package core.gameState.body

import com.fasterxml.jackson.annotation.JsonCreator
import combat.battle.position.HitLevel
import combat.battle.position.TargetPosition
import combat.battle.position.Horizontal
import combat.battle.position.Vertical
import core.gameState.Target
import core.history.display
import core.utility.Named

class BodyPart(override val name: String, val position: TargetPosition = TargetPosition(), slots: List<String> = listOf()) : Named {

    constructor(base: BodyPart) : this(base.name, base.position, base.equippedItems.keys.toList())

    @JsonCreator constructor(name: String, vertical: Vertical, horizontal: Horizontal, slots: List<String> = listOf()) : this(name, TargetPosition(horizontal, vertical), slots)

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

    fun getHitLevel(target: TargetPosition): HitLevel {
        return position.getHitLevel(target)
    }

}