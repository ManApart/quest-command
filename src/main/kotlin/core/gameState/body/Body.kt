package core.gameState.body

import combat.battle.position.HitLevel
import combat.battle.position.TargetPosition
import core.gameState.Target
import core.history.display
import core.utility.NameSearchableList
import java.lang.IllegalArgumentException

class Body(val name: String = "None", parts: List<BodyPart> = listOf()) {
    constructor(base: Body) : this(base.name, base.parts.map { BodyPart(it) })

    private val parts = NameSearchableList(parts)

    private fun hasAttachPoint(attachPoint: String): Boolean {
        return parts.any { it.hasAttachPoint(attachPoint) }
    }

    fun getEquippedItems(): NameSearchableList<Target> {
        val items = NameSearchableList<Target>()
        parts.forEach { part ->
            part.getEquippedItems().forEach { item ->
                if (!items.contains(item)) {
                    items.add(item)
                }
            }
        }
        return items
    }

    fun isEquipped(item: Target) : Boolean {
        return getEquippedItems().contains(item)
    }

    fun getEquippedItemsAt(attachPoint: String): List<Target> {
        return parts.asSequence().map { it.getEquippedItem(attachPoint) }.filterNotNull().toList()
    }

    fun getPart(part: String): BodyPart {
        return parts.get(part)
    }

    private fun getPartsWithAttachPoint(attachPoint: String): List<BodyPart> {
        return parts.filter { it.hasAttachPoint(attachPoint) }
    }

    private fun getPartsEquippedWith(item: Target): List<BodyPart> {
        return parts.filter { it.getEquippedItems().contains(item) }
    }

    fun canEquip(slot: Slot): Boolean {
        return slot.attachPoints.all { hasAttachPoint(it) }
    }

    fun equip(item: Target) {
        equip(item, getDefaultSlot(item))
    }

    fun getDefaultSlot(item: Target): Slot {
        return getEmptyEquipSlot(item)
                ?: item.equipSlots.firstOrNull { canEquip(it) }
                ?: throw IllegalArgumentException("Found no Slot for $item for body $name. This should not happen!")
    }

    fun getEmptyEquipSlot(item: Target) : Slot? {
        return item.equipSlots.firstOrNull { canEquip(it) && it.isEmpty(this) }
    }

    fun equip(item: Target, slot: Slot) {
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

    fun unEquip(item: Target) {
        getPartsEquippedWith(item).forEach {
            it.unEquip(item)
        }
    }

    fun getDirectParts(target: TargetPosition): List<BodyPart> {
        val parts = mutableListOf<BodyPart>()
        this.parts.forEach {
            if (it.position.equals(target)) {
                parts.add(it)
            }
        }
        return parts
    }

    fun getGrazedParts(target: TargetPosition): List<BodyPart> {
        val parts = mutableListOf<BodyPart>()
        this.parts.forEach {
            if (it.position.getHitLevel(target) == HitLevel.GRAZING) {
                parts.add(it)
            }
        }
        return parts
    }

}