package core.commands

import core.utility.NameSearchableList

class ResponseRequest(val message: String, responses: Map<String, String>) {
    constructor(responses: Map<String, String>) : this("", responses)
    private val responses: Map<String, String> = processResponses(responses)
    private val responseKeys = NameSearchableList.from(this.responses.keys.map { it.lowercase() })

    private fun processResponses(responses: Map<String, String>): Map<String, String> {
        val newMap = mutableMapOf<String, String>()
        responses.forEach {
            val newKey = it.key.lowercase()
            when (newKey) {
                "yes" -> newMap["y"] = it.value
                "y" -> newMap["yes"] = it.value
                "no" -> newMap["n"] = it.value
                "n" -> newMap["no"] = it.value
            }
            newMap[newKey] = it.value

        }
        return newMap
    }

    fun getCommand(input: String): String? {
        val cleaned = input.trim().lowercase()

        if (responseKeys.existsExact(cleaned)) {
            return responses[responseKeys.getOrNull(cleaned)!!.name]
        }

        val numberResponse = getNumberResponse(cleaned)
        if (numberResponse != null) {
            return numberResponse
        }

        if (cleaned.toIntOrNull() != null && cleaned.toInt() <= responses.keys.size) {
            return responses[responseKeys[cleaned.toInt() - 1].name]
        }

        return null
    }

    private fun getNumberResponse(input: String): String? {
        if (responseKeys.exists("#") && input.toIntOrNull() != null) {
            return responses["#"]?.replace("#", input)
        }
        return null
    }

    fun getOptions(): List<String> {
        return responseKeys.map { it.name }.toList()
    }

    companion object {
        fun new(message: String, keys: List<String>, values: List<String>): ResponseRequest {
            if (keys.size != values.size) {
                throw IllegalArgumentException("Keys and values must have the same number of items!")
            }
            val responseMap = keys.indices.associate { i -> keys[i] to values[i]}

            return ResponseRequest(message, responseMap)
        }
    }


}