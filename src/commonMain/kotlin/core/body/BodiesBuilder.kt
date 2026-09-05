package core.body

import core.utility.NameSearchableList
import core.utility.toNameSearchableList

class BodiesBuilder {
    internal val children = mutableListOf<BodyBuilder>()
    fun body(body: BodyBuilder) {
        children.add(body)
    }

    fun body(name: String, initializer: BodyBuilder.() -> Unit) {
        children.add(BodyBuilder(name).apply(initializer))
    }
    fun body(name: String, vararg parts: String) {
        children.add(BodyBuilder(name).apply { parts(parts.toList()) })
    }
}

fun bodies(vararg names: String) = names.map { BodyBuilder(it) }
fun bodies(initializer: BodiesBuilder.() -> Unit): List<BodyBuilder> {
    return BodiesBuilder().apply(initializer).children
}

fun List<BodyBuilder>.build(): NameSearchableList<Body> {
    val builders = associateBy { it.name }
    return builders.values.map {
        try {
            it.build()
        } catch (e: Exception) {
            println("Failed to build ${it.name}: ${e.message ?: e.cause ?: e.toString()}")
            throw e
        }
    }.toNameSearchableList()
}
