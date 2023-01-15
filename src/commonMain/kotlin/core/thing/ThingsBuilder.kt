package core.thing

import core.utility.NameSearchableList
import core.utility.applySuspending
import core.utility.toNameSearchableList

class ThingsBuilder {
    internal val children = mutableListOf<ThingBuilder>()

    fun thing(item: ThingBuilder) {
        children.add(item)
    }

    suspend fun thing(name: String, initializer: suspend ThingBuilder.() -> Unit) {
        children.add(ThingBuilder(name).applySuspending(initializer))
    }
}

suspend fun things(initializer: suspend ThingsBuilder.() -> Unit): List<ThingBuilder> {
    return ThingsBuilder().applySuspending(initializer).children
}

suspend fun List<ThingBuilder>.build(tagToApply: String? = null): NameSearchableList<Thing> {
    val tags = tagToApply?.let { listOf(it) } ?: listOf()
    val builders = associateBy { it.name }
    return builders.values.map {
        try {
            it.buildWithBase(builders, tags)
        } catch (e: Exception) {
            println("Failed to build ${it.name}: ${e.message ?: e.cause ?: e.toString()}")
            throw  e
        }
    }.toNameSearchableList()
}
