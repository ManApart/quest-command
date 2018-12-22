package core.gameState

import core.utility.mapAHasAllOfMapB
import core.utility.mapsMatch

class PropertyValues(properties: Map<String, String> = HashMap()) {
    constructor(base: PropertyValues) : this(base.properties)

    private val properties = parseProperties(properties)

    override fun toString(): String {
        return properties.toString()
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
        return mapsMatch(properties, other.properties)
    }

    fun hasAll(other: PropertyValues): Boolean {
        return mapAHasAllOfMapB(properties, other.properties)
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

    fun isEmpty() : Boolean{
        return properties.isEmpty()
    }

}