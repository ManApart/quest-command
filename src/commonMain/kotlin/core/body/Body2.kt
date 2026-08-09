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

    fun getSize(scale: Float): Vector {
        return (dimensions * scale)
    }

    fun getHeight(scale: Float): Int {
        return (dimensions.z * scale).roundToInt()
    }

    fun canEquip(item: Thing): EquipTarget? {
        return null
    }

    fun canEquip(item: Thing, target: EquipTarget): Boolean {
        return false
    }

    fun equip(itemEquipped: Thing, target: EquipTarget) {

    }

    fun unEquip(item: Thing) {

    }

}
