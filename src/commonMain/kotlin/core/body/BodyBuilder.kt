package core.body

import core.utility.toNameSearchableList
import crafting.material.DEFAULT_MATERIAL
import crafting.material.Material
import crafting.material.MaterialManager
import traveling.position.NO_VECTOR
import traveling.position.Vector

data class BodyBuilder(internal val name: String, private var material: String = DEFAULT_MATERIAL.name) {
    private var dimensions = NO_VECTOR
    private val parts = mutableMapOf<String, BodyPartBuilder>()

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
        parts[name] = BodyPartBuilder(name)
    }

    fun part(name: String, material: String) {
        parts[name] = BodyPartBuilder(name).apply { mat(material) }
    }

    fun part(name: String, initializer: BodyPartBuilder.() -> Unit) {
        parts[name] = BodyPartBuilder(name).apply(initializer)
    }

    fun removePart(name: String) {
        parts.remove(name)
    }

    fun build(): Body {
        val mat = MaterialManager.getMaterial(material)
        val bodyParts = parts.values.map { it.build(mat) }.takeIf { it.isNotEmpty() } ?: listOf(BodyPart(name, mat))
        return Body(name, mat, dimensions, bodyParts.toNameSearchableList())
    }
}

fun body(name: String, vararg parts: String): Body {
    return BodyBuilder(name).apply { parts(parts.toList()) }.build()
}

fun body(name: String, initializer: BodyBuilder.() -> Unit): Body {
    return bodyB(name, initializer).build()
}

fun bodyB(name: String, vararg parts: String): BodyBuilder {
    return BodyBuilder(name).apply { parts(parts.toList()) }
}

fun bodyB(name: String, initializer: BodyBuilder.() -> Unit): BodyBuilder {
    return BodyBuilder(name).apply(initializer)
}

fun unBuild(body: Body): BodyBuilder.() -> Unit {
    with(body) {
        return {
            dimensions(getDimensions())
            mat(baseMaterial.name)
            parts.forEach { part(it.name, it.material.name) }
        }
    }
}
