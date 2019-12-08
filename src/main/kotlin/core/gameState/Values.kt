package core.gameState

import core.utility.apply
import core.utility.hasAllOf
import core.utility.matches

//TODO - give property keys optional max/min values (like burnhealth should not go below 0)
class Values(properties: Map<String, String> = mapOf()) {
    constructor(base: Values, params: Map<String, String> = mapOf()) : this(base.properties.apply(params))

    private val properties = parseProperties(properties)

    override fun toString(): String {
        return if (properties.isEmpty()) {
            ""
        } else {
            properties.entries.joinToString(", ") { "${it.key} - ${it.value}" }
        }
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
            return try {
                Integer.parseInt(properties[key.toLowerCase()])
            } catch (e: NumberFormatException) {
                default
            }
        }
        return default
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        if (properties.containsKey(key.toLowerCase())) {
            return properties[key.toLowerCase()]!!.toBoolean()
        }
        return default
    }

    fun getString(key: String, default: String = ""): String {
        return properties[key.toLowerCase()] ?: default
    }

    fun getList(key: String, delimiter: String = ",", default: List<String> = listOf()): List<String> {
        return properties[key.toLowerCase()]?.split(delimiter) ?: default
    }

    fun put(key: String, value: String) {
        properties[key.toLowerCase()] = value
    }

    fun put(key: String, value: Int) {
        properties[key.toLowerCase()] = value.toString()
    }

    fun put(key: String, value: Boolean) {
        properties[key.toLowerCase()] = value.toString()
    }

    fun matches(other: Values): Boolean {
        return properties.matches(other.properties)
    }

    fun has(value: String): Boolean {
        return properties.containsKey(value.toLowerCase())
    }

    fun hasInt(value: String): Boolean {
        return has(value) && getInt(value, Int.MAX_VALUE) != Int.MAX_VALUE
    }

    fun hasAll(other: Values): Boolean {
        return properties.hasAllOf(other.properties)
    }

    fun isEmpty(): Boolean {
        return properties.isEmpty()
    }

    fun getAll(): Map<String, String> {
        return properties.toMap()
    }

    fun setFrom(other: Values) {
        other.properties.forEach { properties[it.key] = it.value }
    }

    fun inc(key: String, amount: Int) {
        properties[key.toLowerCase()] = (getInt(key, 0) + amount).toString()
    }

}