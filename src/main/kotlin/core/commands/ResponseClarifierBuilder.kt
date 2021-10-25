package core.commands

import core.Player

class ResponseClarifierBuilder(private val source: Player) {
    private val responses = mutableMapOf<String, RequestResponseBuilder>()

    fun respond(key: String, initializer: RequestResponseBuilder.() -> Unit) {
        responses[key] = RequestResponseBuilder().apply(initializer)
    }

    fun build(): ResponseClarifier {
        val builtResponses = responses.entries.associate { it.key to it.value.build() }
        return ResponseClarifier(source, builtResponses)
    }
}

fun Player.clarify(initializer: ResponseClarifierBuilder.() -> Unit): ResponseClarifier {
    return ResponseClarifierBuilder(this).apply(initializer).build()
}