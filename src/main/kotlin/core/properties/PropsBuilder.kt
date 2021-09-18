package core.properties

import core.utility.MapBuilder
import core.utility.apply

class PropsBuilder {
    private val tags = mutableListOf<String>()
    private val values: MapBuilder = MapBuilder()
    private val paramsBuilder = MapBuilder()

    fun tag(vararg tags: String) {
        this.tags.addAll(tags)
    }

    fun value(values: Map<String, Any>) = this.values.entry(values)
    fun value(vararg values: Pair<String, Any>) = this.values.entry(values.toList())
    fun value(key: String, value: String) = values.entry(key, value)
    fun value(key: String, value: Int) = values.entry(key, value)

    fun param(vararg values: Pair<String, Any>) = this.paramsBuilder.entry(values.toList())
    fun param(values: Map<String, Any>) = this.paramsBuilder.entry(values.toList())
    fun param(key: String, value: String) = paramsBuilder.entry(key, value)
    fun param(key: String, value: Int) = paramsBuilder.entry(key, value)

    fun props(properties: Properties){
        tags.addAll(properties.tags.getAll())
        value(properties.values.getAll())
    }

    internal fun build(): Properties {
        val params = paramsBuilder.build()
        val appliedTags = tags.apply(params)
        val appliedValues = values.build().toMutableMap().apply(params)
        return Properties(Values(appliedValues), Tags(appliedTags))
    }

    fun build(bases: List<PropsBuilder> = listOf()): Properties {
        val built = (bases + listOf(this)).map { it.build() }
        return built.reduce { acc, props ->
            acc.overrideWith(props)
            acc
        }
    }
}

fun props(initializer: PropsBuilder.() -> Unit): Properties {
    return PropsBuilder().apply(initializer).build()
}

fun propsUnbuilt(initializer: PropsBuilder.() -> Unit): PropsBuilder {
    return PropsBuilder().apply(initializer)
}