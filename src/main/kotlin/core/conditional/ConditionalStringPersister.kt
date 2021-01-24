package core.conditional

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

fun getPersisted(dataObject: ConditionalStringPointer): Map<String, Any> {
    val string = jacksonObjectMapper().writeValueAsString(dataObject)
    val data: MutableMap<String, Any> = jacksonObjectMapper().readValue(string)
    data["version"] = 1
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): ConditionalStringPointer {
    return jacksonObjectMapper().readValue(jacksonObjectMapper().writeValueAsString(data))
}

