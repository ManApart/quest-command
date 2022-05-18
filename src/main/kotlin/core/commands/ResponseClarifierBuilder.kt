package core.commands

import core.Player
import core.history.displayToMe

class ResponseClarifierBuilder(private val source: Player) {
    private val responses = mutableMapOf<String, RequestResponseBuilder>()

    fun respond(key: String, defaultBehavior: () -> Unit, initializer: RequestResponseBuilder.() -> Unit) {
        responses[key] = RequestResponseBuilder(defaultBehavior).apply(initializer)
    }

    fun respond(key: String, noOptionMessage: String, initializer: RequestResponseBuilder.() -> Unit) {
        responses[key] = RequestResponseBuilder { source.displayToMe(noOptionMessage) }.apply(initializer)
    }

    fun respond(key: String, initializer: RequestResponseBuilder.() -> Unit) {
        responses[key] = RequestResponseBuilder { }.apply(initializer)
    }

    fun build(): ResponseClarifier {
        //Are we just moving the problem to having missing keys?
        val builtResponses = responses.mapValues { it.value.build() }.entries.filter { it.value != null }.associate { it.key to it.value!! }
        return ResponseClarifier(source, builtResponses)
    }
}

fun Player.clarify(initializer: ResponseClarifierBuilder.() -> Unit): ResponseClarifier {
    return ResponseClarifierBuilder(this).apply(initializer).build()
}