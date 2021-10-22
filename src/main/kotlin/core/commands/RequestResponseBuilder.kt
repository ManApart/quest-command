package core.commands

import core.Player

class RequestResponseBuilder {
    private var message: String = ""
    private val options = mutableListOf<String>()
    private var line: (String) -> String = { "" }

    fun message(message: String) {
        this.message = message
    }

    fun options(vararg option: String) {
        this.options.addAll(option)
    }

    fun options(options: List<String>) {
        this.options.addAll(options)
    }

    fun command(line: (String) -> String) {
        this.line = line
    }

    fun build(): ResponseRequest {
        val fullMessage = "$message\n\t${options.joinToString(", ")}"
        val messageOptions = options.associateWith(line)
        return ResponseRequest(fullMessage, messageOptions)
    }
}

fun Player.respond(initializer: RequestResponseBuilder.() -> Unit) {
    val response = RequestResponseBuilder().apply(initializer).build()
    CommandParsers.setResponseRequest(this, response)
}