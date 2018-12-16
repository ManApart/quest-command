package building.json

class JsonConverter(data: List<MutableMap<String, Any>>) {
    init {
        validate(data)
    }

    private val data = buildNamedList(data)

    private fun validate(data: List<MutableMap<String, Any>>) {
        data.forEach { item ->
            if (!item.containsKey("name") || item["name"] !is String) {
                throw IllegalArgumentException("Item didn't have a string value for 'name': $item")
            }

            if (item.containsKey("extends") && item["extends"] !is String) {
                throw IllegalArgumentException("Item didn't have a string value for 'extends': $item")
            }
        }

        data.filter { it.containsKey(("extends")) }.forEach { item ->
            if (data.none { it["name"] == item["extends"] }) {
                throw IllegalArgumentException("Item extends a non-real item: $item")
            }
        }
    }

    private fun buildNamedList(data: List<MutableMap<String, Any>>): Map<String, MutableMap<String, Any>> {
        val map = mutableMapOf<String, MutableMap<String, Any>>()
        data.forEach {
            map[it["name"] as String] = it
        }
        return map
    }

    fun transform(): List<Map<String, Any>> {
        val results = mutableListOf<MutableMap<String, Any>>()

        data.values.forEach {
            extend(it)
        }

        data.values.forEach { results.add(it) }
        return results
    }

    private fun extend(item: MutableMap<String, Any>) {
        if (item.containsKey("extends")) {
            val base = data[item["extends"] as String]!!
            extend(base)
            transfer(base, item)
            item.remove("extends")
        }
    }

    private fun transfer(base: MutableMap<String, Any>, item: MutableMap<String, Any>) {
        base.entries.forEach { (key, value) ->
            transferProperty(item, key, value)
        }
    }

    private fun transferProperty(item: MutableMap<String, Any>, baseKey: String, baseValue: Any) {
        if (!item.containsKey(baseKey)) {
            item[baseKey] = baseValue
        } else if (baseValue is List<*>) {
            val newList = baseValue.toMutableList()
            newList.addAll((item[baseKey] as List<*>))
            item[baseKey] = newList
        } else if (baseValue is Map<*, *>) {
            transfer(baseValue as MutableMap<String, Any>, item[baseKey] as MutableMap<String, Any>)
        }
    }

}