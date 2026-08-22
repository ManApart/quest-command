package core.body

import core.properties.TagStrings.WEAPON
import core.thing.Thing
import core.utility.Named
import crafting.material.DEFAULT_MATERIAL
import crafting.material.Material

val NO_PART = BodyPart("None", DEFAULT_MATERIAL)

data class BodyPart(override val name: String, val material: Material) : Named {
    private val equipped = mutableMapOf<Layer, Thing>()

    fun equip(item: Thing, layer: Layer) {
        equipped[layer] = item
    }

    fun getEquipped() = equipped.values.toSet().toList()
    fun getEquipped(layer: Layer) = equipped[layer]

    fun unEquip(item: Thing) {
        equipped.filter { it.value == item }.forEach { equipped.remove(it.key) }
    }

    fun getEquippedWeapon() = equipped.values.firstOrNull { it.properties.tags.has(WEAPON) }
}
