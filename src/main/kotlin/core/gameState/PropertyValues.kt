package core.gameState

import core.utility.mapsMatch

class PropertyValues(properties: Map<String, String> = HashMap()) {
    private val properties = parseProperties(properties)

    private fun parseProperties(properties: Map<String, String>): MutableMap<String, String> {
        val parsed = mutableMapOf<String, String>()
        properties.entries.forEach {
            parsed[it.key.toLowerCase()] = it.value.toLowerCase()
        }
        return parsed
    }

    fun getInt(key: String, default: Int = 0): Int {
        if (properties.containsKey(key.toLowerCase())) {
            return Integer.parseInt(properties[key.toLowerCase()])
        }
        return default
    }

    fun getString(key: String, default: String = ""): String {
        return properties[key.toLowerCase()] ?: default
    }

    fun matches(other: PropertyValues): Boolean {
        return mapsMatch(properties, other.properties)
    }

    fun inherit(parent: PropertyValues) {
        parent.properties.forEach{
            if (!properties.containsKey(it.key)){
                properties[it.key] = it.value
            }
        }
    }

    fun applyParams(params: Map<String, String>): PropertyValues {
        val newProps = core.utility.applyParams(properties, params)
        return PropertyValues(newProps)
    }
}