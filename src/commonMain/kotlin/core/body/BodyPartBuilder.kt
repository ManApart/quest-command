package core.body

import crafting.material.Material
import crafting.material.MaterialManager

class BodyPartBuilder(private val name: String) {
    private var material: String? = null

    fun mat(m: String) {
        material = m
    }

    fun build(defaultMaterial: Material): BodyPart {
        val mat = material?.let { MaterialManager.getMaterial(it) } ?: defaultMaterial
        return BodyPart(name, mat)
    }
}

fun unBuild(part: BodyPart): BodyPartBuilder {
    return BodyPartBuilder(part.name).apply { mat(part.material.name) }
}
