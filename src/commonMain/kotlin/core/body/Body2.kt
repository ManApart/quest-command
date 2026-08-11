package core.body

import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.Named
import core.utility.max
import traveling.position.NO_VECTOR
import traveling.position.Vector
import kotlin.math.roundToInt

//scale is passed in from prop on parent

data class Body2(
    override val name: String = "None",
    private val dimensions: Vector = NO_VECTOR,
    val parts: NameSearchableList<BodyPart>
) : Named {

    override fun toString(): String {
        return name + ": [" + parts.joinToString { it.name } + "]"
    }

    fun getSize(scale: Double): Vector {
        return (dimensions * scale)
    }

    fun getHeight(scale: Double): Int {
        return (dimensions.z * scale).roundToInt()
    }

    fun getRange(scale: Double): Int {
        val size = getSize(scale) * 2
        return max(size.x, size.y, size.z) / 2
    }

    fun equipOptions(item: Thing): List<EquipTarget> {
        return item.equipTargets.filter { canEquip(it) }
    }

    fun getEmptyEquipOptions(item: Thing): List<EquipTarget> {
        return item.equipTargets.filter { canEquipWithoutUnequippingOther(it) }
    }

    fun getDefaultTarget(item: Thing): EquipTarget? {
        return getEmptyEquipOptions(item).firstOrNull() ?: equipOptions(item).firstOrNull()
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

    fun equip(item: Thing, target: EquipTarget) {
        target.parts.mapNotNull { parts.getOrNull(it) }.forEach {
            it.unEquip(target.layer)
            it.equip(item, target.layer)
        }
    }

    fun findEquipTarget(item: Thing, part: String, layer: String): EquipTarget? {
        return item.equipTargets.firstOrNull { t ->
            t.layer.lowercase().contains(layer) && t.parts.any { it.lowercase().contains(part) }
        }
    }

    fun isEquipped(item: Thing) = getEquipped().contains(item)
    fun getEquipped() = parts.flatMap { it.getEquipped() }.toSet().toList()

    fun getEquippedAt(target: EquipTarget): List<Thing> {
        return target.parts.mapNotNull { getEquippedAt(it, target.layer) }
    }
    fun getEquippedAt(part: String, layer: Layer): Thing? {
        return parts.getOrNull(part)?.getEquipped(layer)
    }

    fun unEquip(item: Thing) {
        parts.forEach { it.unEquip(item) }
    }

}
