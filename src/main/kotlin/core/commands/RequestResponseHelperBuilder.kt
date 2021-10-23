package core.commands

import core.Player

class RequestResponseHelperBuilder(private val source: Player) {
    private val responses = mutableMapOf<String, RequestResponseBuilder>()

    fun respond(key: String, initializer: RequestResponseBuilder.() -> Unit) {
        responses[key] = RequestResponseBuilder().apply(initializer)
    }

    fun build(): ResponseRequestHelper? {
//        val builtResponses = responses.entries.map {  }
//        return ResponseRequestHelper(source, )
        return null
    }
}

fun Player.respondWrapper(initializer: RequestResponseHelperBuilder.() -> Unit): ResponseRequestHelper {
    return RequestResponseHelperBuilder(this).apply(initializer).build()!!
}