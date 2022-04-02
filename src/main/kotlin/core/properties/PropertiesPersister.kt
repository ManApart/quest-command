package core.properties

@kotlinx.serialization.Serializable
data class PropertiesP(
    val tags: List<String>,
    val values: Map<String, String>
) {
    constructor(b: Properties) : this(b.tags.getAll(), b.values.getAll())

    fun parsed(): Properties {
        val props = Properties()
        props.tags.addAll(tags)
        values.entries.forEach { (key, value) ->
            props.values.put(key, value)
        }
        return props
    }
}