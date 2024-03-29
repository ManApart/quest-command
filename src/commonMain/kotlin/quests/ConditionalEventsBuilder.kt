package quests

import core.events.Event
import core.utility.MapBuilder
import core.utility.applySuspending
import kotlin.reflect.KClass

class ConditionalEventsBuilder<E: Event>(private val trigger: KClass<E>) {
    private val paramsBuilder = MapBuilder()
    private var condition: (E, Map<String, String>) -> Boolean = { _, _ -> true }
    private var createEvents: suspend (E, Map<String, String>) -> List<Event> = { _, _ -> listOf() }

    fun build(): ConditionalEvents<E> {
        return ConditionalEvents(trigger, condition, createEvents, paramsBuilder.build())
    }

    fun condition(cond: (E, Map<String, String>) -> Boolean){
        this.condition = cond
    }

    fun events(createEvents: suspend (E, Map<String, String>) -> List<Event>){
        this.createEvents = createEvents
    }

    fun param(vararg values: Pair<String, Any>) = this.paramsBuilder.entry(values.toList())
    fun param(key: String, value: String) = paramsBuilder.entry(key, value)
    fun param(key: String, value: Int) = paramsBuilder.entry(key, value)
    
}

fun <E: Event> condition(trigger: KClass<E>, initializer: ConditionalEventsBuilder<E>.() -> Unit): ConditionalEvents<*> {
    return ConditionalEventsBuilder(trigger).apply(initializer).build()
}