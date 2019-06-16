package core.commands

import core.utility.NameSearchableList

class ResponseRequest(responses: Map<String, String>) {
    private val responses: Map<String, String> = processResponses(responses)
    private val responseKeys = NameSearchableList.from(this.responses.keys.map { it.toLowerCase() })

    private fun processResponses(responses: Map<String, String>): Map<String, String> {
        val newMap = mutableMapOf<String, String>()
        responses.forEach {
            val newKey = it.key.toLowerCase()
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
        val cleaned = input.trim().toLowerCase()

        if (cleaned.toIntOrNull() != null && cleaned.toInt() <= responses.keys.size) {
            return responses[responseKeys[cleaned.toInt()-1].name]
        }

        if (responseKeys.exists(cleaned)) {
            return responses[responseKeys.getOrNull(cleaned)!!.name]
        }

        return null
    }

    fun getOptions(): List<String>{
        return responseKeys.map { it.name }.toList()
    }

    companion object {
        fun new(keys: List<String>, values: List<String>) : ResponseRequest {
            if (keys.size != values.size) {
                throw IllegalArgumentException("Keys and values must have the same number of items!")
            }
            val responseMap = mutableMapOf<String, String>()
            for (i in 0 until keys.size){
                responseMap[keys[i]] = values[i]
            }
            return ResponseRequest(responseMap)
        }
    }


}