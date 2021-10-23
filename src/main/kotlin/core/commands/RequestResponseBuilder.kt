package core.commands

import core.Player
import core.utility.Named

class RequestResponseBuilder {
    private var message: String? = null
    private val options = mutableListOf<String>()
    private val displayedOptions = mutableListOf<String>()
    private var line: ((String) -> String)? = null

    fun message(message: String) {
        this.message = message
    }

    fun options(vararg option: String) {
        this.options.addAll(option)
    }

    fun options(options: List<String>) {
        this.options.addAll(options)
    }

    @JvmName("optionsNamed")
    fun options(options: List<Named>) {
        this.options.addAll(options.map { it.name })
    }

    /**
     * Overrides the visual display of options. Must be the same length as options
     */
    fun displayedOptions(vararg option: String) {
        this.displayedOptions.addAll(option)
    }

    fun displayedOptions(options: List<String>) {
        this.displayedOptions.addAll(options)
    }

    fun command(line: (String) -> String) {
        this.line = line
    }

    fun build(): ResponseRequest {
        if (message == null || options.isEmpty() || line == null) throw Exception("Response request cannot have null values.")
        if (displayedOptions.isNotEmpty() && displayedOptions.size != options.size) throw Exception("Displayed options must be same size as options.")

        val usedOptions = displayedOptions.ifEmpty { options }

        val fullMessage = "$message\n\t${usedOptions.joinToString(", ")}"
        val messageOptions = options.associateWith(line!!)
        return ResponseRequest(fullMessage, messageOptions)
    }
}

fun Player.respond(initializer: RequestResponseBuilder.() -> Unit) {
    val response = RequestResponseBuilder().apply(initializer).build()
    CommandParsers.setResponseRequest(this, response)
}