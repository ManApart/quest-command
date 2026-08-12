package core.commands

import core.utility.capitalizePhrase
import core.utility.toNameSearchableListOfStrings
import kotlin.math.max

typealias DisplayOption = String
typealias CommandToRun = String

class ResponseRequest(
    val message: String,
    responses: Map<DisplayOption, CommandToRun>,
    private val value: String? = null,
    private val useDefault: Boolean = false,
    private val defaultValue: String? = null
) {
    private val responses: Map<DisplayOption, CommandToRun> = processResponses(responses)
    private val responseKeys = this.responses.keys.map { it.lowercase() }.toNameSearchableListOfStrings()

    private fun processResponses(responses: Map<DisplayOption, CommandToRun>): Map<DisplayOption, CommandToRun> {
        val newMap = mutableMapOf<DisplayOption, CommandToRun>()
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

    override fun toString(): String {
        return "$value:$message"
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
            val adjusted = max(0,input.toInt()).toString()
            return responses["#"]?.replace("#", adjusted)
        }
        return null
    }

    fun getOptions(): List<String> {
        return responseKeys.map { it.name.capitalizePhrase() }.toList()
    }

    fun hasValue(): Boolean {
        return !value.isNullOrBlank() || (useDefault && defaultValue != null)
    }

    fun getValue(): String? {
        return value ?: defaultValue
    }

}
