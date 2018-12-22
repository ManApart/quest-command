package core.utility

fun Map<String, String>.inheritMap(parent: Map<String, String>): Map<String, String> {
    val newMap = toMutableMap()
    parent.forEach{
        if (!newMap.containsKey(it.key)){
            newMap[it.key] = it.value
        }
    }
    return newMap
}

fun Map<String, String>.overrideMap(overrides: Map<String, String>): Map<String, String> {
    val newMap = toMutableMap()
    overrides.forEach{
        newMap[it.key] = it.value
    }
    return newMap
}

fun Map<String, String>.applyParams(params: Map<String, String>) : Map<String, String> {
    val newMap = mutableMapOf<String, String>()

    entries.forEach {
        val key = it.key.replaceParams(params)
        val value = it.value.replaceParams(params)
        newMap[key] = value
    }
    return newMap
}

fun String.replaceParams(paramValues: Map<String, String>): String {
    var modified = this
    paramValues.forEach {
        modified = modified.replace("$${it.key}", it.value)
    }
    return modified
}

fun Map<String, String>.toEmptyString() : String {
    return if (isEmpty()) {
        ""
    } else {
        toString()
    }
}