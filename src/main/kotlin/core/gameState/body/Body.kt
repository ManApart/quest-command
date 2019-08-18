package core.gameState.body

import combat.battle.position.HitLevel
import combat.battle.position.TargetPosition
import core.gameState.Direction
import core.gameState.Target
import core.gameState.location.LocationNode
import core.gameState.location.Network
import core.history.display
import core.utility.NameSearchableList
import core.utility.Named
import java.lang.IllegalArgumentException

val NONE = Body("None")

class Body(override val name: String = "None", val layout: Network = Network(name)) : Named {
    constructor(base: Body) : this(base.name, base.layout)

    private val parts = NameSearchableList(layout.getLocations().asSequence().map { it.bodyPart }.filterNotNull().toList())

    override fun toString(): String {
        return name +": [" + layout.getLocations().joinToString{it.name} +"]"
    }

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

    fun isEquipped(item: Target): Boolean {
        return getEquippedItems().contains(item)
    }

    fun getEquippedItemsAt(attachPoint: String): List<Target> {
        return parts.asSequence().map { it.getEquippedItem(attachPoint) }.filterNotNull().toList()
    }

    fun hasPart(part: String): Boolean {
        return parts.exists(part)
    }

    fun getPart(part: String): BodyPart {
        return parts.get(part)
    }

    fun getParts(): List<BodyPart> {
        return parts.toList()
    }

    fun getAnyParts(names: List<String>): List<BodyPart> {
        return parts.getAny(names)
    }

    fun getPartLocation(part: String): LocationNode {
        return layout.getLocationNode(part)
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

    fun getEmptyEquipSlot(item: Target): Slot? {
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

    fun getClimbEntryParts(): List<LocationNode> {
        return layout.getFurthestLocations(Direction.BELOW)
    }

}