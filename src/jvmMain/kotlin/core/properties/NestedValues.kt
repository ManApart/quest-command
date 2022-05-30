package core.properties

import core.utility.hasAllOf
import core.utility.matches

class NestedValues(properties: Map<String, Map<String, String>> = mapOf()) {
//    constructor(base: NestedValues, params: Map<String, Map<String, String>> = mapOf()) : this(base.properties.apply(params))

    private val properties = parseProperties(properties)

    override fun toString(): String {
        return if (properties.isEmpty()) {
            ""
        } else {
            properties.entries.joinToString(", ") { "${it.key} - ${it.value}" }
        }
    }

    private fun parseProperties(properties: Map<String, Map<String, String>>): MutableMap<String, MutableMap<String, String>> {
        val parsed = mutableMapOf<String, MutableMap<String, String>>()
        properties.entries.forEach { outerEntry ->
            val map = mutableMapOf<String, String>()
            outerEntry.value.entries.forEach {
                map[it.key.lowercase()] = it.value.lowercase()
            }
            parsed[outerEntry.key.lowercase()] = map
        }
        return parsed
    }

    fun getInt(group: String, key: String, default: Int = 0): Int {
        if (properties.containsKey(key.lowercase())) {
            return try {
                if (hasInt(group, key)) {
                    Integer.parseInt(properties[group.lowercase()]!![key.lowercase()])
                } else {
                    default
                }
            } catch (e: NumberFormatException) {
                default
            }
        }
        return default
    }

    fun getBoolean(group: String, key: String, default: Boolean = false): Boolean {
        if (has(group, key)) {
            return properties[group.lowercase()]!![key.lowercase()]!!.toBoolean()
        }
        return default
    }

    fun getString(group: String, key: String, default: String = ""): String {
        return properties[group.lowercase()]?.get(key.lowercase()) ?: default
    }

    fun put(group: String, key: String, value: String) {
        properties.putIfAbsent(group, mutableMapOf())
        properties[group.lowercase()]!![key.lowercase()] = value
    }

    fun put(group: String, key: String, value: Int) {
        properties.putIfAbsent(group, mutableMapOf())
        properties[group.lowercase()]!![key.lowercase()] = value.toString()
    }

    fun put(group: String, key: String, value: Boolean) {
        properties.putIfAbsent(group, mutableMapOf())
        properties[group.lowercase()]!![key.lowercase()] = value.toString()
    }

    fun clear(group: String) {
        properties.remove(group)
    }

    fun clear(group: String, key: String) {
        properties[group]?.remove(key)
    }

    fun matches(other: NestedValues): Boolean {
        return properties.matches(other.properties)
    }

    fun has(group: String, key: String): Boolean {
        return properties[group.lowercase()]?.containsKey(key.lowercase()) ?: false
    }

    fun hasInt(group: String, key: String): Boolean {
        return has(group, key) && getInt(group, key, Int.MAX_VALUE) != Int.MAX_VALUE
    }

    fun hasAll(other: NestedValues): Boolean {
        return properties.hasAllOf(other.properties)
    }

    fun isEmpty(): Boolean {
        return properties.isEmpty()
    }

    fun getAll(): Map<String, Map<String, String>> {
        return properties.toMap()
    }

    fun clear() {
        properties.clear()
    }

    fun setFrom(other: NestedValues) {
        other.properties.forEach { properties[it.key] = it.value }
    }

    fun inc(group:String, key: String, amount: Int) {
        val newAmount = getInt(group, key, 0) + amount
        put(group, key, newAmount)
    }

}