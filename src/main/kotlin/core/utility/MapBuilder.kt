package core.utility

class MapBuilder {
    private val values = mutableMapOf<String, String>()

    fun entry(values: List<Pair<String, Any>>) {
        values.forEach { (key, value) ->
            this.values[key] = value.toString()
        }
    }

    fun entry(vararg entry: Pair<String, String>) {
        entry.forEach { (key, value) ->
            values[key] = value
        }
    }

    internal fun build(): Map<String, String> {
        return values.toMap()
    }
}

fun props(initializer: MapBuilder.() -> Unit): Map<String, String> {
    return MapBuilder().apply(initializer).build()
}