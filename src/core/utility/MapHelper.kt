package core.utility

fun inheritMap(parent: Map<String, String>, child: Map<String, String>): Map<String, String> {
    val newMap = child.toMutableMap()
    parent.forEach{
        if (!newMap.containsKey(it.key)){
            newMap[it.key] = it.value
        }
    }
    return newMap
}

fun overrideMap(map: Map<String, String>, overrides: Map<String, String>): Map<String, String> {
    val newMap = map.toMutableMap()
    overrides.forEach{
        newMap[it.key] = it.value
    }
    return newMap
}

fun applyParams(map: Map<String, String>, params: Map<String, String>) : Map<String, String> {
    val newMap = mutableMapOf<String, String>()

    map.entries.forEach {
        val key = replaceParams(it.key, params)
        val value = replaceParams(it.value, params)
        newMap[key] = value
    }
    return newMap
}

fun replaceParams(line: String, paramValues: Map<String, String>): String {
    var modified = line
    paramValues.forEach {
        modified = modified.replace("$${it.key}", it.value)
    }
    return modified
}