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

    fun entry(key: String, value: String) {
        values[key] = value
    }

    fun entry(key: String, value: Int) {
        values[key] = value.toString()
    }

    fun build(bases: List<MapBuilder> = listOf()): Map<String, String> {
        val all = bases + listOf(this)
        val result = all.first().values.toMutableMap()
        all.subList(1, all.size).forEach { next ->
            next.values.forEach { (key, value) -> result[key] = value }
        }
        return result
    }
}

fun map(initializer: MapBuilder.() -> Unit): Map<String, String> {
    return MapBuilder().apply(initializer).build()
}

fun mapUnbuilt(initializer: MapBuilder.() -> Unit): MapBuilder {
    return MapBuilder().apply(initializer)
}