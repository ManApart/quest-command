package core.target

import core.utility.NameSearchableList
import core.utility.toNameSearchableList

class TargetsBuilder {
    internal val children = mutableListOf<TargetBuilder>()

    fun target(item: TargetBuilder) {
        children.add(item)
    }

    fun target(name: String, initializer: TargetBuilder.() -> Unit) {
        children.add(TargetBuilder(name).apply(initializer))
    }
}

fun targets(initializer: TargetsBuilder.() -> Unit): List<TargetBuilder> {
    return TargetsBuilder().apply(initializer).children
}

fun List<TargetBuilder>.build(tagToApply: String? = null): NameSearchableList<Target> {
    val builders = associateBy { it.name }
    return builders.values.map {
        try {
            it.buildWithBase(builders)
        } catch (e: Exception) {
            println("Failed to build ${it.name}: ${e.message ?: e.cause ?: e.toString()}")
            throw  e
        }
    }.toNameSearchableList().also { list ->
        if (tagToApply != null) list.forEach { it.properties.tags.add(tagToApply) }
    }
}
