package core.body

import core.utility.Named
import crafting.material.Material

data class BodyPart(override val name: String, val material: Material) : Named
