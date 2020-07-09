package core.utility

fun Map<String, String>.inheritMap(parent: Map<String, String>): Map<String, String> {
    val newMap = toMutableMap()
    parent.forEach {
        if (!newMap.containsKey(it.key)) {
            newMap[it.key] = it.value
        }
    }
    return newMap
}

fun Map<String, String>.overrideMap(overrides: Map<String, String>): Map<String, String> {
    val newMap = toMutableMap()
    overrides.forEach {
        newMap[it.key] = it.value
    }
    return newMap
}

fun Map<String, String>.apply(params: Map<String, String>): Map<String, String> {
    val newMap = mutableMapOf<String, String>()

    entries.forEach {
        val key = it.key.apply(params)
        val value = it.value.apply(params)
        newMap[key] = value
    }
    return newMap
}

fun Map<String, String>.toEmptyString(): String {
    return if (isEmpty()) {
        ""
    } else {
        toString()
    }
}

// Does this work on nested maps / recursively?
fun <A, B> Map<A, B>.matches(other: Map<A, B>): Boolean {
    keys.forEach {
        if (!other.containsKey(it)) {
            return false
        } else if (this[it] != other[it]) {
            return false
        }
    }
    other.keys.forEach {
        if (!containsKey(it)) {
            return false
        } else if (this[it] != other[it]) {
            return false
        }
    }
    return true
}

fun <A, B> Map<A, B>.hasAllOf(mapB: Map<A, B>): Boolean {
    mapB.keys.forEach {
        if (!containsKey(it)) {
            return false
        } else if (mapB[it] != this[it]) {
            return false
        }
    }
    return true
}

fun <A, B> Map<A, B>.getAllStrings(): List<String> {
    val strings = mutableListOf<String>()
    addAllStrings(strings)
    return strings
}

private fun <A, B> Map<A, B>.addAllStrings(strings: MutableList<String>) {
    entries.forEach {
        if (it.key is String) {
            strings.add(it.key as String)
            if (it.value is String) {
                strings.add(it.value as String)
            } else if (it.value is Map<*, *>) {
                (it.value as Map<*, *>).addAllStrings(strings)
            }
        }
    }
}

fun <A, B> MutableMap<A, MutableList<B>>.putList(key: A, value: B) {
    if (!containsKey(key)){
        this[key] = mutableListOf()
    }
    this[key]!!.add(value)
}