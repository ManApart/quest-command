package core.body

import com.fasterxml.jackson.annotation.JsonCreator
import combat.block.BlockHelper
import core.history.display
import core.target.Target
import core.utility.NameSearchableList
import core.utility.Named
import core.utility.max
import traveling.direction.Direction
import traveling.position.Vector
import traveling.location.Network
import traveling.location.location.Location
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode

val NONE = Body("None")

class Body(override val name: String = "None", val layout: Network = Network(name), private val slotMap: MutableMap<String, String> = mutableMapOf()) : Named {

    constructor(base: Body) : this(base.name, Network(base.layout))

    @JsonCreator
    constructor(name: String, bodyPart: LocationRecipe) : this(name, Network(name, bodyPart))

    private val parts: NameSearchableList<Location> by lazy { createParts() }
    val blockHelper = BlockHelper()

    private fun createParts(): NameSearchableList<Location> {
        return NameSearchableList(layout.getLocationNodes().map { it.getLocation() })
    }

    override fun toString(): String {
        return name + ": [" + parts.joinToString { it.name } + "]"
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

    fun getPart(part: String): Location {
        return parts.get(part)
    }

    fun getParts(): List<Location> {
        return parts.toList()
    }

    fun getAnyParts(names: List<String>): List<Location> {
        return parts.getAny(names)
    }

    fun getPartLocation(part: String): LocationNode {
        return layout.getLocationNode(part)
    }

    fun getRootPart(): Location {
        return layout.rootNode.getLocation()
    }

    private fun getPartsWithAttachPoint(attachPoint: String): List<Location> {
        return parts.filter { it.hasAttachPoint(attachPoint) }
    }

    private fun getPartsEquippedWith(item: Target): List<Location> {
        return parts.filter { it.getEquippedItems().contains(item) }
    }

    fun canEquip(item: Target, slot: Slot = getDefaultSlot(item)): Boolean {
        return canEquip(slot)
    }

    fun canEquip(slot: Slot): Boolean {
        return slot.attachPoints.all {
            hasAttachPoint(it)
        }
    }

    private fun hasAttachPoint(attachPoint: String): Boolean {
        return parts.any {
            it.hasAttachPoint(attachPoint)
        }
    }

    fun getDefaultSlot(item: Target): Slot {
        return getEmptyEquipSlot(item)
                ?: item.equipSlots.firstOrNull { canEquip(it) }
                ?: throw IllegalArgumentException("Found no Slot for $item for body $name. This should not happen!")
    }

    fun getEmptyEquipSlot(item: Target): Slot? {
        return item.equipSlots.sortedBy {
            it.attachPoints.any { point ->
                point.contains("Right")
            }
        }
                .reversed()
                .firstOrNull {
                    canEquip(it) && it.isEmpty(this)
                }
    }

    fun equip(item: Target, slot: Slot = getDefaultSlot(item)) {
        if (canEquip(slot)) {
            unEquip(item)
            slotMap[item.name] = slot.description
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
        slotMap.remove(item.name)
        getPartsEquippedWith(item).forEach {
            it.unEquip(item)
        }
    }

    fun getClimbEntryParts(): List<LocationNode> {
        return layout.getFurthestLocations(Direction.BELOW)
    }

    fun getPositionInLocation(part: Location, parentOffset: Vector): Vector {
        return parentOffset + Vector(z = layout.rootNodeHeight) + (layout.rootNode.getVectorDistanceTo(getPartLocation(part.name)))
    }

    fun getSize(): Vector {
        return layout.getSize()
    }

    fun getRange(): Int {
        val size = getSize()
        return max(size.x, size.y, size.z) / 2
    }

    fun getSlotMap(): Map<String, String> {
        return slotMap.toMap()
    }

}