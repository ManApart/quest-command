package core.body

import core.thing.Thing
import core.utility.Named
import crafting.material.Material



data class BodyPart(override val name: String, val material: Material) : Named {
    val equipped = mutableMapOf<Layer, Thing>()
}
