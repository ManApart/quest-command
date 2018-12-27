package building.json

import core.utility.getAllStrings

class JsonConverter(data: List<MutableMap<String, Any>>) {
    private val data = buildNamedList(data)

    init {
        validate()
    }

    private fun buildNamedList(data: List<MutableMap<String, Any>>): Map<String, MutableMap<String, Any>> {
        val map = mutableMapOf<String, MutableMap<String, Any>>()
        data.forEach { item ->
            if (!item.containsKey("name") || item["name"] !is String) {
                throw IllegalArgumentException("Object didn't have a string value for 'name': $item")
            }
            map[item["name"] as String] = item
        }
        return map
    }

    private fun validate() {
        data.values.forEach { item ->
            if (item.containsKey("extends") && item["extends"] !is String) {
                throw IllegalArgumentException("Object didn't have a string value for 'extends': $item")
            }
        }

        data.values.filter { it.containsKey(("extends")) }.forEach { item ->
            if (data.keys.none { it == item["extends"] }) {
                throw IllegalArgumentException("Object extends a non-real item: $item")
            }
        }

        data.values.forEach { item ->
            val variables = getExtendedVariables(item)
            if (variables.isNotEmpty()) {
                val params = getExtendedParams(item)
                variables.forEach {
                    if (!params.contains(it)) {
                        throw IllegalArgumentException("Object had unsatisfied variable $it: $item")
                    }
                }
            }
        }
    }

    private fun getExtendedParams(item: MutableMap<String, Any>): List<String> {
        val params = mutableListOf<String>()
        addExtendedParams(item, params)
        return params
    }

    private fun addExtendedParams(item: MutableMap<String, Any>, params: MutableList<String>) {
        params.addAll(getParams(item))
        if (item.containsKey("extends")) {
            val base = data[item["extends"] as String]
            if (base != null) {
                addExtendedParams(base, params)
            }
        }
    }

    private fun getParams(item: MutableMap<String, Any>): List<String> {
        return if (item.containsKey("params") && item["params"] is Map<*, *>) {
            (item["params"] as Map<*, *>).keys.asSequence().filter { it is String }.map { it as String }.toList()
        } else {
            listOf()
        }
    }

    private fun getExtendedVariables(item: MutableMap<String, Any>): List<String> {
        val variables = mutableListOf<String>()
        addExtendedVariables(item, variables)
        return variables
    }

    private fun addExtendedVariables(item: MutableMap<String, Any>, variables: MutableList<String>) {
        variables.addAll(getVariables(item))
        if (item.containsKey("extends")) {
            val base = data[item["extends"] as String]
            if (base != null) {
                addExtendedParams(base, variables)
            }
        }
    }

    private fun getVariables(item: MutableMap<String, Any>): List<String> {
        return item.getAllStrings().asSequence().filter { it.startsWith("\$") }.map { it.substring(1, it.length) }.toList()
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
            @Suppress("UNCHECKED_CAST")
            transfer(baseValue as MutableMap<String, Any>, item[baseKey] as MutableMap<String, Any>)
        }
    }

}