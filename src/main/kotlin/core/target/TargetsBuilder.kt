package core.target

import core.utility.NameSearchableList
import core.utility.toNameSearchableList

class TargetsBuilder {
    internal val children = mutableListOf<TargetBuilder>()

    fun target(name: String, initializer: TargetBuilder.() -> Unit) {
        children.add(TargetBuilder(name).apply(initializer))
    }
}

fun targets(initializer: TargetsBuilder.() -> Unit): List<TargetBuilder> {
    return TargetsBuilder().apply(initializer).children
}

fun List<TargetBuilder>.build() : NameSearchableList<Target> {
    val builders = associateBy { it.name }
    return builders.values.map {
        try {
            it.buildWithBase(builders)
        } catch (e: Exception) {
            println("Failed to build ${it.name}: ${e.message ?: e.cause ?: e.toString()}")
            throw  e
        }
    }.toNameSearchableList()
}
