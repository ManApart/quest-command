package core.body

import crafting.material.DEFAULT_MATERIAL
import crafting.material.MaterialManager
import traveling.position.NO_VECTOR
import traveling.position.Vector

class BodyBuilder(internal val name: String) {
    private var dimensions = NO_VECTOR
    private var material = DEFAULT_MATERIAL.name
    private val parts = mutableListOf<BodyPartBuilder>()

    fun dimensions(width: Int, length: Int, height: Int) =
        dimensions(Vector(width, length, height))

    fun dimensions(v: Vector) {
        dimensions = v
    }

    fun mat(m: String) {
        material = m
    }

    fun parts(vararg names: String) = names.forEach { part(it) }
    fun part(name: String) {
        parts.add(BodyPartBuilder(name))
    }

    fun part(name: String, initializer: BodyPartBuilder.() -> Unit) {
        parts.add(BodyPartBuilder(name).apply(initializer))
    }

    fun build(): Body2 {
        val mat = MaterialManager.getMaterial(material)
        val bodyParts = parts.map { it.build(mat) }
        return Body2(name, dimensions, bodyParts)
    }
}

fun body(name: String, initializer: BodyBuilder.() -> Unit): Body2 {
    return BodyBuilder(name).apply(initializer).build()
}
