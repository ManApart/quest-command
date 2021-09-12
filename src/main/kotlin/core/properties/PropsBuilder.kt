package core.properties

import core.utility.MapBuilder

class PropsBuilder {
    private val tags = mutableListOf<String>()
    private val values: MapBuilder = MapBuilder()

    fun tag(vararg tags: String) {
        this.tags.addAll(tags)
    }

    fun value(vararg values: Pair<String, Any>) = this.values.entry(values.toList())
    fun value(key: String, value: String) = values.entry(key, value)
    fun value(key: String, value: Int) = values.entry(key, value)

    internal fun build(): Properties {
        return Properties(Values(values.build()), Tags(tags))
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