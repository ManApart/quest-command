package core.properties

class PropsBuilder {
    private val tags = mutableListOf<String>()
    private val values = mutableMapOf<String, String>()

    fun tag(vararg tags: String){
        this.tags.addAll(tags)
    }

    fun value(values: Map<String, Any>){
        values.entries.forEach { (key, value)-> this.values[key] = value.toString() }
    }

    fun value(key: String, value: String){
        values[key] = value
    }

    fun value(key: String, value: Int){
        values[key] = value.toString()
    }

    internal fun build(): Properties {
        return Properties(Values(values), Tags(tags))
    }
}

fun props(initializer: PropsBuilder.() -> Unit): Properties {
    return PropsBuilder().apply(initializer).build()
}