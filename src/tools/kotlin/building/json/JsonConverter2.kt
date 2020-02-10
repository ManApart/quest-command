package building.json

class JsonConverter2(data: List<JsonFileConversion>) {
    private val conversionMap = mutableMapOf<String, JsonFileConversion>()
    private val allData = buildNamedList(data)

    init {
        validate()
    }

    private fun buildNamedList(data: List<JsonFileConversion>): Map<String, MutableMap<String, Any>> {
        val map = mutableMapOf<String, MutableMap<String, Any>>()
        data.forEach { conversion ->
            conversion.data.forEach { item ->
                if (!item.containsKey("name") || item["name"] !is String) {
                    throw IllegalArgumentException(conversion.inputPath + " didn't have a string value for 'name': $item")
                }
                if (map.containsKey(item["name"])) {
                    val previousEntry = conversionMap[item["name"]]!!
                    throw IllegalArgumentException("Name must be unique but is duplicated in ${previousEntry.inputPath} and ${conversion.inputPath}: $item")
                }
                map[item["name"] as String] = item
                conversionMap[item["name"] as String] = conversion
            }
        }
        return map
    }

    private fun validate() {
        allData.values.forEach { item ->
            if (item.containsKey("extends") && item["extends"] !is String) {
                val entry = conversionMap[item["name"]]!!
                throw IllegalArgumentException("${entry.inputPath} didn't have a string value for 'extends': $item")
            }
        }

        allData.values.filter { it.containsKey(("extends")) }.forEach { item ->
            if (allData.keys.none { it == item["extends"] }) {
                val entry = conversionMap[item["name"]]!!
                throw IllegalArgumentException("${entry.inputPath} extends a non-real item: $item")
            }
        }
    }

    fun transform(data: JsonFileConversion): List<Map<String, Any>> {
        val results = mutableListOf<MutableMap<String, Any>>()

        data.data.forEach {
            extend(it)
        }

        data.data.forEach { results.add(it) }
        return results
    }

    private fun extend(item: MutableMap<String, Any>) {
        if (item.containsKey("extends")) {
            val base = allData[item["extends"] as String] ?: error("Key did not exist")
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
            @Suppress("UNCHECKED_CAST")
            transfer(baseValue as MutableMap<String, Any>, item[baseKey] as MutableMap<String, Any>)
        }
    }

}