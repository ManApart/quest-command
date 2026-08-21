package core.body

import combat.block.BlockHelper
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.Named
import core.utility.max
import core.utility.toNameSearchableList
import traveling.position.NO_VECTOR
import traveling.position.Vector
import kotlin.math.roundToInt

val NONE = Body()

data class Body(
    override val name: String = "None",
    private val dimensions: Vector = NO_VECTOR,
    val parts: NameSearchableList<BodyPart> = emptyList<BodyPart>().toNameSearchableList(),
) : Named {
    val core = parts.first()
    val blockHelper = BlockHelper()
    val equippedItems = mutableMapOf<Thing, EquippedItem>()

    override fun toString(): String {
        return name + ": [" + parts.joinToString { it.name } + "]"
    }

    fun getSize(scale: Double): Int {
        return getDimensions(scale).getDistance()
    }

    fun getDimensions(scale: Double): Vector {
        return (dimensions * scale)
    }

    fun getDimensionsUnscaled(): Vector {
        return dimensions
    }

    fun getHeight(scale: Double): Int {
        return (dimensions.z * scale).roundToInt()
    }

    fun getRange(scale: Double): Int {
        val size = getDimensions(scale) * 2
        return max(size.x, size.y, size.z) / 2
    }

    fun equipOptions(item: Thing): List<EquipTarget> {
        return item.equipTargets.filter { canEquip(it) }
    }

    fun emptyEquipOptions(item: Thing): List<EquipTarget> {
        return item.equipTargets.filter { canEquipWithoutUnequippingOther(it) }
    }

    fun getDefaultTarget(item: Thing): EquipTarget? {
        return emptyEquipOptions(item).firstOrNull() ?: equipOptions(item).firstOrNull()
    }

    fun canEquip(item: Thing): Boolean {
        return item.equipTargets.firstOrNull { canEquip(it) } != null
    }

    fun canEquip(target: EquipTarget): Boolean {
        return target.parts.all { parts.getOrNull(it) != null }
    }

    fun canEquipWithoutUnequippingOther(target: EquipTarget): Boolean {
        return target.parts.all {
            val part = parts.getOrNull(it)
            part != null && part.getEquipped(target.layer) == null
        }
    }

    fun equipToEmpty(item: Thing) = emptyEquipOptions(item).firstOrNull()?.let { equip(item, it) }
    fun equip(item: Thing) = equip(item, getDefaultTarget(item)!!)
    fun equip(item: Thing, part: String, layer: String) = equip(item, EquipTarget(layer, listOf(part)))
    fun equip(item: Thing, target: EquipTarget) = equip(item.toEquippedItem(target, parts))
    private fun equip(item: EquippedItem): EquippedItem {
        equippedItems[item.item] = item
        item.parts.forEach {
            it.unEquip(item.layer)
            it.equip(item.item, item.layer)
        }
        return item
    }

    fun findEquipTarget(item: Thing, part: String): EquipTarget? {
        return item.equipTargets.firstOrNull { t ->
            t.parts.any { it.lowercase().contains(part) }
        }
    }

    fun findEquipTargetByLayer(item: Thing, layer: String): EquipTarget? {
        return item.equipTargets.firstOrNull { t ->
            t.layer.lowercase().contains(layer)
        }
    }

    fun findEquipTarget(item: Thing, part: String, layer: String): EquipTarget? {
        return item.equipTargets.firstOrNull { t ->
            t.layer.lowercase().contains(layer) && t.parts.any { it.lowercase().contains(part) }
        }
    }

    fun isEquipped(item: Thing) = getEquipped().contains(item)
    fun getEquipped() = equippedItems.values.map { it.item }

    fun getEquippedAt(target: EquipTarget): List<Thing> {
        return target.parts.mapNotNull { getEquippedAt(it, target.layer) }
    }

    fun getEquippedAt(part: String, layer: Layer): Thing? {
        return parts.getOrNull(part)?.getEquipped(layer)
    }

    fun getEquippedAt(layer: Layer): List<Thing> {
        return parts.mapNotNull { it.getEquipped(layer) }
    }

    fun getEquippedTarget(item: Thing) = equippedItems[item]

    fun unEquip(item: Thing) {
        equippedItems.remove(item)
        parts.forEach { it.unEquip(item) }
    }

    fun getAllMaterials() = parts.map { it.material }.toSet().toList()

}
