package core.body

import combat.block.BlockHelper
import core.history.display
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.Named
import core.utility.max
import crafting.material.DEFAULT_MATERIAL
import crafting.material.Material
import traveling.direction.Direction
import traveling.location.Network
import traveling.location.location.Location
import traveling.location.network.LocationNode
import traveling.position.Vector

val NONE = Body("None", DEFAULT_MATERIAL)

data class Body(
    override val name: String = "None",
    val material: Material = DEFAULT_MATERIAL,
    val layout: Network = Network(name),
    private val slotMap: MutableMap<String, String> = mutableMapOf()
) : Named {

    constructor(base: Body) : this(base.name, base.material, Network(base.layout))

    private var partsBacking: NameSearchableList<Location>? = null
    private suspend fun parts(): NameSearchableList<Location> {
        return partsBacking ?: createParts().also { partsBacking = it }
    }

    //    private val parts: NameSearchableList<Location> by lazy { createParts() }
    val blockHelper = BlockHelper()

    private suspend fun createParts(): NameSearchableList<Location> {
        return NameSearchableList(layout.getLocationNodes().map { it.getLocation() })
    }

    override fun toString(): String {
        //Since we can't run suspending in our tostring
        val parts = partsBacking ?: emptyList()
        return name + ": [" + parts.joinToString { it.name } + "]"
    }

    suspend fun equipItems(equippedItems: List<Thing>) {
        equippedItems.forEach { item ->
            val slotName = slotMap[item.name]
            val slot = item.equipSlots.firstOrNull { equipSlot -> equipSlot.description == slotName }
            if (slot != null) {
                equip(item, slot)
            } else {
                equip(item)
            }
        }
    }

    suspend fun getEquippedItems(): NameSearchableList<Thing> {
        val items = NameSearchableList<Thing>()
        parts().forEach { part ->
            part.getEquippedItems().forEach { item ->
                if (!items.contains(item)) {
                    items.add(item)
                }
            }
        }
        return items
    }

    suspend fun isEquipped(item: Thing): Boolean {
        return getEquippedItems().contains(item)
    }

    suspend fun getEquippedItemsAt(attachPoint: String): List<Thing> {
        return parts().asSequence().map { it.getEquippedItem(attachPoint) }.filterNotNull().toList()
    }

    suspend fun hasPart(part: String): Boolean {
        return parts().exists(part)
    }

    suspend fun getPart(part: String): Location {
        return parts().get(part)
    }

    suspend fun getPartOrNull(part: String): Location? {
        return if (hasPart(part)) parts().get(part) else null
    }

    suspend fun getParts(): List<Location> {
        return parts().toList()
    }

    suspend fun getAnyParts(names: List<String>): List<Location> {
        return parts().getAny(names)
    }

    fun getPartLocation(part: String): LocationNode {
        return layout.getLocationNode(part)
    }

    suspend fun getRootPart(): Location {
        return layout.rootNode.getLocation()
    }

    private suspend fun getPartsWithAttachPoint(attachPoint: String): List<Location> {
        return parts().filter { it.hasAttachPoint(attachPoint) }
    }

    private suspend fun getPartsEquippedWith(item: Thing): List<Location> {
        return parts().filter { it.getEquippedItems().contains(item) }
    }

    suspend fun canEquip(item: Thing, slotIn: Slot? = null): Boolean {
        val slot = slotIn ?: getDefaultSlot(item)
        return canEquip(slot)
    }

    suspend fun canEquip(slot: Slot): Boolean {
        return slot.attachPoints.all {
            hasAttachPoint(it)
        }
    }

    private suspend fun hasAttachPoint(attachPoint: String): Boolean {
        return parts().any {
            it.hasAttachPoint(attachPoint)
        }
    }

    suspend fun getDefaultSlot(item: Thing): Slot {
        return getEmptyEquipSlot(item)
            ?: item.equipSlots.firstOrNull { canEquip(it) }
            ?: throw IllegalArgumentException("Found no slot for $item for body $name. This should not happen!")
    }

    suspend fun getEmptyEquipSlot(item: Thing): Slot? {
        val options = item.equipSlots.filter { canEquip(it) && it.isEmpty(this) }
        val nonHandOption = options.firstOrNull {
            it.attachPoints.none { point ->
                point.contains("Grip")
            }
        }
        val rightHandFirst = options.sortedBy {
            it.attachPoints.any { point ->
                point.contains("Right")
            }
        }.reversed()
        return nonHandOption ?: rightHandFirst.firstOrNull()
//        return item.equipSlots.sortedBy {
//            it.attachPoints.any { point ->
//                point.contains("Right")
//            }
//        }.reversed()
//            .firstOrNull {
//                canEquip(it) && it.isEmpty(this)
//            }
    }

    suspend fun equip(item: Thing, slotIn: Slot? = null) {
        val slot = slotIn ?: getDefaultSlot(item)
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
            item.display("Can't equip ${item.name} to ${slot.description}")
        }
    }

    suspend fun unEquip(item: Thing) {
        item.location.getLocation().removeThing(item)
        slotMap.remove(item.name)
        getPartsEquippedWith(item).forEach {
            it.unEquip(item)
        }
    }

    fun getClimbEntryParts(): List<LocationNode> {
        return layout.getFurthestLocations(Direction.BELOW)
    }

    fun getPositionInLocation(part: Location, parentOffset: Vector): Vector {
        return parentOffset + Vector(z = layout.rootNodeHeight) + (layout.rootNode.getVectorDistanceTo(
            getPartLocation(
                part.name
            )
        ))
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