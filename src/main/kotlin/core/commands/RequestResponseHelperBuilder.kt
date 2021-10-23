package core.commands

import core.Player

class RequestResponseHelperBuilder(private val source: Player) {
    private val responses = mutableMapOf<String, RequestResponseBuilder>()

    fun respond(key: String, initializer: RequestResponseBuilder.() -> Unit) {
        responses[key] = RequestResponseBuilder().apply(initializer)
    }

    fun build(): ResponseRequestHelper {
        val builtResponses = responses.entries.associate { it.key to it.value.build() }
        return ResponseRequestHelper(source, builtResponses)
    }
}

fun Player.responseHelper(initializer: RequestResponseHelperBuilder.() -> Unit): ResponseRequestHelper {
    return RequestResponseHelperBuilder(this).apply(initializer).build()
}