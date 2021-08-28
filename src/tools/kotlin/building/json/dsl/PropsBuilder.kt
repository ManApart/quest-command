package building.json.dsl

import core.properties.Properties
import core.properties.Tags
import core.properties.Values

class PropsBuilder {
    private val tags = mutableListOf<String>()
    private val values = mutableMapOf<String, Any>()

    fun tag(vararg tags: String){
        this.tags.addAll(tags)
    }

    fun value(values: Map<String, Any>){
        values.entries.forEach { (key, value)-> this.values[key] = value }
    }

    fun value(key: String, value: String){
        values[key] = value
    }

    fun value(key: String, value: Int){
        values[key] = value
    }

    internal fun build(): Properties {
        return Properties(Values(), Tags(tags))
    }
}

fun props(initializer: PropsBuilder.() -> Unit): Properties {
    return PropsBuilder().apply(initializer).build()
}