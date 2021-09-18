package core.properties

import core.utility.MapBuilder
import core.utility.apply

class PropsBuilder {
    private val tags = mutableListOf<String>()
    private val values: MapBuilder = MapBuilder()

    fun tag(vararg tags: String) {
        this.tags.addAll(tags)
    }

    fun value(values: Map<String, Any>) = this.values.entry(values)
    fun value(vararg values: Pair<String, Any>) = this.values.entry(values.toList())
    fun value(key: String, value: String) = values.entry(key, value)
    fun value(key: String, value: Int) = values.entry(key, value)

    fun props(properties: Properties){
        tags.addAll(properties.tags.getAll())
        value(properties.values.getAll())
    }

    internal fun build(params: Map<String, String> = mapOf()): Properties {
        val appliedTags = tags.apply(params).toMutableList()
        val appliedValues = values.build().toMutableMap().apply(params)
        return Properties(Values(appliedValues), Tags(appliedTags))
    }

    fun build(bases: List<PropsBuilder> = listOf(), params: Map<String, String> = mapOf()): Properties {
        val built = (bases + listOf(this)).map { it.build(params) }
        return built.reduce { acc, props ->
            acc.overrideWith(props)
            acc
        }
    }
}

fun props(params: Map<String, String> =  mapOf(), initializer: PropsBuilder.() -> Unit): Properties {
    return PropsBuilder().apply(initializer).build(params)
}

fun propsUnbuilt(initializer: PropsBuilder.() -> Unit): PropsBuilder {
    return PropsBuilder().apply(initializer)
}