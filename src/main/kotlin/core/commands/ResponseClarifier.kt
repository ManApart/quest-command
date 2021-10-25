package core.commands

import core.Player

class ResponseClarifier(private val player: Player, private val responses: Map<String, ResponseRequest>) {

    fun hasAllValues(): Boolean {
        return responses.values.all {
            it.hasValue()
        }
    }

    fun getIntValue(key: String): Int {
        return responses[key]?.getValue()?.toIntOrNull() ?: 0
    }

    fun getStringValue(key: String): String {
        return responses[key]?.getValue() ?: ""
    }

    fun requestAResponse() {
        val response = responses.values.first { !it.hasValue() }
        CommandParsers.setResponseRequest(player, response)
    }
}