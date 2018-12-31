package core.commands

class ResponseRequest(responses: Map<String, String>) {
    private val responseKeys = responses.keys.map { it.toLowerCase() }
    private val responses: Map<String, String> = processResponses(responses)

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
        if (responses.containsKey(cleaned)) {
            return responses[cleaned]
        }

        //Get Nth response
        if (cleaned.toIntOrNull() != null && cleaned.toInt() <= responses.keys.size) {
            return responses[responseKeys[cleaned.toInt()-1]]
        }
        return null
    }

}