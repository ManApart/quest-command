package core.gameState

import core.utility.apply
import core.utility.hasAllOf
import core.utility.matches
import core.utility.toEmptyString

class PropertyValues(properties: Map<String, String> = mapOf()) {
    constructor(base: PropertyValues, params: Map<String, String> = mapOf()) : this(base.properties.apply(params))

    private val properties = parseProperties(properties)

    override fun toString(): String {
        return properties.toEmptyString()
    }

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
        return properties.matches(other.properties)
    }

    fun hasAll(other: PropertyValues): Boolean {
        return properties.hasAllOf(other.properties)
    }

    fun inherit(parent: PropertyValues) {
        parent.properties.forEach {
            if (!properties.containsKey(it.key)) {
                properties[it.key] = it.value
            }
        }
    }

    fun isEmpty(): Boolean {
        return properties.isEmpty()
    }

}