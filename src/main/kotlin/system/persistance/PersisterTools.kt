package system.persistance

fun getMapKey(data: Map<String, Any>, key: String) : Map<String, Any> {
    return if (data.containsKey(key)){
        @Suppress("UNCHECKED_CAST")
        (data[key] as Map<String, Any>)
    } else {
        mapOf()
    }
}

fun getListKey(data: Map<String, Any>, key: String) : List<String> {
    return if (data.containsKey(key)){
        @Suppress("UNCHECKED_CAST")
        (data[key] as List<String>)
    } else {
        listOf()
    }
}

fun getListMapKey(data: Map<String, Any>, key: String) : List<Map<String, Any>> {
    return if (data.containsKey(key)){
        @Suppress("UNCHECKED_CAST")
        (data[key] as List<Map<String, Any>>)
    } else {
        listOf()
    }
}