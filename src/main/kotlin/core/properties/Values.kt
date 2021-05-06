package core.properties

import core.properties.GuardManager.getGuardedValue
import core.utility.apply
import core.utility.hasAllOf
import core.utility.matches

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
            parsed[it.key.lowercase()] = it.value.lowercase()
        }
        return parsed
    }

    fun getInt(key: String, default: Int = 0): Int {
        if (properties.containsKey(key.lowercase())) {
            return try {
                Integer.parseInt(properties[key.lowercase()])
            } catch (e: NumberFormatException) {
                default
            }
        }
        return default
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        if (properties.containsKey(key.lowercase())) {
            return properties[key.lowercase()]!!.toBoolean()
        }
        return default
    }

    fun getString(key: String, default: String = ""): String {
        return properties[key.lowercase()] ?: default
    }

    fun getList(key: String, delimiter: String = ",", default: List<String> = listOf()): List<String> {
        return properties[key.lowercase()]?.split(delimiter) ?: default
    }

    fun put(key: String, value: String) {
        val lowKey = key.lowercase()
        val guarded = getGuardedValue(lowKey, getString(lowKey), value)
        properties[lowKey] = guarded
    }

    fun put(key: String, value: Int) {
        val lowKey = key.lowercase()
        val guarded = getGuardedValue(lowKey, getString(lowKey), value.toString())
        properties[lowKey] = guarded
    }

    fun put(key: String, value: Boolean) {
        val lowKey = key.lowercase()
        val guarded = getGuardedValue(lowKey, getString(lowKey), value.toString())
        properties[lowKey] = guarded
    }

    fun clear(key: String) {
        properties.remove(key)
    }

    fun matches(other: Values): Boolean {
        return properties.matches(other.properties)
    }

    fun has(value: String): Boolean {
        return properties.containsKey(value.lowercase())
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

    fun clear() {
        properties.clear()
    }

    fun setFrom(other: Values) {
        other.properties.forEach { properties[it.key] = it.value }
    }

    fun inc(key: String, amount: Int) {
        put(key.lowercase(), (getInt(key, 0) + amount).toString())
    }

}