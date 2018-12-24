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