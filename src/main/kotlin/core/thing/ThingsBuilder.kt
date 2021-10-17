package core.thing

import core.utility.NameSearchableList
import core.utility.toNameSearchableList

class ThingsBuilder {
    internal val children = mutableListOf<ThingBuilder>()

    fun thing(item: ThingBuilder) {
        children.add(item)
    }

    fun thing(name: String, initializer: ThingBuilder.() -> Unit) {
        children.add(ThingBuilder(name).apply(initializer))
    }
}

fun things(initializer: ThingsBuilder.() -> Unit): List<ThingBuilder> {
    return ThingsBuilder().apply(initializer).children
}

fun List<ThingBuilder>.build(tagToApply: String? = null): NameSearchableList<Thing> {
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
