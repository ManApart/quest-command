package core.commands

import core.Player

class RequestResponseHelperBuilder(private val source: Player) {
    private val responses = mutableMapOf<String, RequestResponseBuilder>()

    fun respond(key: String, initializer: RequestResponseBuilder.() -> Unit) {
        responses[key] = RequestResponseBuilder().apply(initializer)
    }

    fun build(): ResponseRequestWrapper? {
        return null
    }
}

fun Player.respondWrapper(initializer: RequestResponseHelperBuilder.() -> Unit): ResponseRequestWrapper {
    return RequestResponseHelperBuilder(this).apply(initializer).build()!!
}