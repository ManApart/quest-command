package core.body

import core.thing.Thing
import core.utility.Named
import traveling.position.NO_VECTOR
import traveling.position.Vector
import kotlin.math.roundToInt

//scale is passed in from prop on parent

data class Body2(
    override val name: String = "None",
    private val dimensions: Vector = NO_VECTOR,
    val parts: List<BodyPart>
) : Named {
    private val namedParts = parts.associateBy { it.name }

    fun getSize(scale: Float): Vector {
        return (dimensions * scale)
    }

    fun getHeight(scale: Float): Int {
        return (dimensions.z * scale).roundToInt()
    }

    fun equipOptions(item: Thing): List<EquipTarget> {
        return item.equipTargets.filter { canEquip(item, it) }
    }

    fun canEquip(item: Thing, target: EquipTarget): Boolean {
        return target.parts.all { namedParts[it]?.canEquip(item, target.layer) == true }
    }

    fun equip(item: Thing, target: EquipTarget) {
        target.parts.forEach { namedParts[it]?.equip(item, target.layer) }
    }

    fun unEquip(item: Thing) {
        parts.forEach { it.unEquip(item) }
    }

}
