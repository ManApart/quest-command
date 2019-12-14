package core.body

import traveling.direction.Direction
import core.target.Target
import traveling.direction.Vector
import traveling.location.LocationNode
import traveling.location.Network
import core.history.display
import core.utility.NameSearchableList
import core.utility.Named
import core.utility.max

val NONE = Body("None")

class Body(override val name: String = "None", val layout: Network = Network(name)) : Named {
    constructor(base: Body) : this(base.name, Network(base.layout))

    private val parts = createParts()

    private fun createParts(): NameSearchableList<BodyPart> {
        return NameSearchableList(layout.getLocations().asSequence().map { it.bodyPart }.filterNotNull().toList())
    }

    override fun toString(): String {
        return name + ": [" + layout.getLocations().joinToString { it.name } + "]"
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

    fun getRootPart(): BodyPart? {
        return layout.rootNode?.getLocation()?.bodyPart
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

    private fun hasAttachPoint(attachPoint: String): Boolean {
        return parts.any { it.hasAttachPoint(attachPoint) }
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

    fun getClimbEntryParts(): List<LocationNode> {
        return layout.getFurthestLocations(Direction.BELOW)
    }

    fun getPositionInLocation(part: BodyPart, parentOffset: Vector): Vector {
        return parentOffset + Vector(z = layout.rootNodeHeight) + (layout.rootNode?.getVectorDistanceTo(getPartLocation(part.name)) ?: Vector())
    }

    fun getSize(): Vector {
        return layout.getSize()
    }

    fun getRange() : Int {
        val size = getSize()
        return max(size.x, size.y, size.z) / 2
    }

}