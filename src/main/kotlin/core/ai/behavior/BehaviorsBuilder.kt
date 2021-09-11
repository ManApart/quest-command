package core.ai.behavior

import core.events.Event
import quests.ConditionalEventsBuilder

class BehaviorsBuilder {
    internal val children = mutableMapOf<String, ConditionalEventsBuilder<*>>()

    fun behavior(name: String, item: ConditionalEventsBuilder<*>) {
        children[name] = item
    }

    fun <E: Event> behavior(name: String, trigger: Class<E>, initializer: ConditionalEventsBuilder<E>.() -> Unit) {
        children[name] = ConditionalEventsBuilder(trigger).apply(initializer)
    }
}

fun behaviors(initializer: BehaviorsBuilder.() -> Unit): List<Behavior<*>> {
    return BehaviorsBuilder().apply(initializer).children.build()
}

fun Map<String, ConditionalEventsBuilder<*>>.build(): List<Behavior<*>> {
    return map { (name, builder) ->
        try {
            Behavior(name, builder.build())
        } catch (e: Exception) {
            println("Failed to build ${name}: ${e.message ?: e.cause ?: e.toString()}")
            throw  e
        }
    }
}