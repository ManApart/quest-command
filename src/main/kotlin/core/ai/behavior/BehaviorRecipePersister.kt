package core.ai.behavior


fun getPersisted(dataObject: BehaviorRecipe): Map<String, Any> {
    val data: MutableMap<String, Any> = mutableMapOf()
    data["version"] = 1
    data["name"] = dataObject.name
    data["params"] = dataObject.params.toMutableMap()
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): BehaviorRecipe {
    val name = data["name"]!! as String
    val params = data["params"]!! as Map<String, String>

    return BehaviorRecipe(name, params)
}