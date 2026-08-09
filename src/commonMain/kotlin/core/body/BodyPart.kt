package core.body

import core.thing.Thing
import core.utility.Named
import crafting.material.Material


data class BodyPart(override val name: String, val material: Material) : Named {
    private val equipped = mutableMapOf<Layer, Thing>()

    fun canEquip(item: Thing, layer: Layer): Boolean {
        val current = equipped[layer]
        return current == null || current == item
    }

    fun equip(item: Thing, layer: Layer) {
        equipped[layer] = item
    }

    fun getEquipped() = equipped.values.toSet().toList()
    fun getEquipped(layer: Layer) = equipped[layer]

    fun unEquip(item: Thing) {
        equipped.filter { it.value == item }.forEach { equipped.remove(it.key) }
    }
}
