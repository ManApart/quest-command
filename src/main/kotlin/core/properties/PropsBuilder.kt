package core.properties

import core.utility.MapBuilder

class PropsBuilder() {
    private val tags = mutableListOf<String>()
    private val values: MapBuilder = MapBuilder()

    fun tag(vararg tags: String) {
        this.tags.addAll(tags)
    }

    fun value(vararg values: Pair<String, Any>) = this.values.entry(values.toList())
    fun value(key: String, value: String) = values.entry(key to value)
    fun value(key: String, value: Int) = values.entry(key to value.toString())

    internal fun build(): Properties {
        return Properties(Values(values.build()), Tags(tags))
    }

    internal fun build(base: PropsBuilder?): Properties {
        val ourProps = build()
        val inheritedProps = base?.build()?.also { it.overrideWith(ourProps) }
        return inheritedProps ?: ourProps
    }
}

fun props(initializer: PropsBuilder.() -> Unit): Properties {
    return PropsBuilder().apply(initializer).build()
}