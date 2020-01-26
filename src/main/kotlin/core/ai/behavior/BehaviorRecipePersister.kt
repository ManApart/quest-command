package core.ai.behavior

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


fun getPersisted(dataObject: BehaviorRecipe): Map<String, Any> {
    val string = jacksonObjectMapper().writeValueAsString(dataObject)
    val data: MutableMap<String, Any> = jacksonObjectMapper().readValue(string)
    data["version"] = 1
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): BehaviorRecipe {
    return jacksonObjectMapper().readValue(jacksonObjectMapper().writeValueAsString(data))
}