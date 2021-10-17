package core.commands

import core.Player

class ResponseRequestHelper(private val player: Player, private val responses: Map<String, ResponseRequestWrapper>) {

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
        val response = responses.values.first { !it.hasValue() }.responseRequest
        CommandParsers.setResponseRequest(player, response)
    }
}