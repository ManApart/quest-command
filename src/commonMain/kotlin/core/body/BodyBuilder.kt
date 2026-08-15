package core.body

import core.utility.toNameSearchableList
import crafting.material.DEFAULT_MATERIAL
import crafting.material.MaterialManager
import traveling.position.NO_VECTOR
import traveling.position.Vector

class BodyBuilder(internal val name: String, private var material: String = DEFAULT_MATERIAL.name) {
    private var dimensions = NO_VECTOR
    private val parts = mutableListOf<BodyPartBuilder>()

    fun dimensions(width: Int, length: Int, height: Int) =
        dimensions(Vector(width, length, height))

    fun dimensions(v: Vector) {
        dimensions = v
    }

    fun mat(m: String) {
        material = m
    }

    /**
    Note that the first part is considered the core for aiming etc
     */
    fun parts(names: List<String>) = names.forEach { part(it) }
    /**
    Note that the first part is considered the core for aiming etc
     */
    fun parts(vararg names: String) = names.forEach { part(it) }
    fun part(name: String) {
        parts.add(BodyPartBuilder(name))
    }

    fun part(name: String, initializer: BodyPartBuilder.() -> Unit) {
        parts.add(BodyPartBuilder(name).apply(initializer))
    }

    fun build(): Body2 {
        val mat = MaterialManager.getMaterial(material)
        val bodyParts = parts.map { it.build(mat) }.takeIf { it.isNotEmpty() } ?: listOf(BodyPart(name, mat))
        return Body2(name, dimensions, bodyParts.toNameSearchableList())
    }
}

fun body(name: String, vararg parts: String): Body2 {
    return BodyBuilder(name).apply { parts(parts.toList()) }.build()
}

fun body(name: String, initializer: BodyBuilder.() -> Unit): Body2 {
    return BodyBuilder(name).apply(initializer).build()
}
