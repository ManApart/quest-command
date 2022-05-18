package core.commands

import core.Player
import core.utility.Named

class RequestResponseBuilder(private val defaultBehavior: () -> Unit) {
    private var message: String = ""
    private val options = mutableListOf<String>()
    private val displayedOptions = mutableListOf<String>()
    private var line: ((String) -> String) = { it }
    private var useDefault = false
    private var value: String? = null
    private var defaultValue: String? = null

    fun build(): ResponseRequest? {
        if (options.isEmpty()) {
            defaultBehavior()
            return null
        }
        if (displayedOptions.isNotEmpty() && displayedOptions.size != options.size) throw Exception("Displayed options must be same size as options.")

        val optionsForMessage = displayedOptions.ifEmpty { options }

        val additionalOptions = displayedOptions.mapIndexed { i, displayedOption -> displayedOption to options[i] }.toMap()

        val fullMessage = "$message\n\t${optionsForMessage.joinToString(", ")}"
        val messageOptions = options.associateWith(line) + additionalOptions
        return ResponseRequest(fullMessage, messageOptions, value, useDefault, defaultValue)
    }

    fun message(message: String) {
        this.message = message
    }

    fun options(vararg option: Int) {
        this.options.addAll(option.map { it.toString() })
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

    fun yesNoOptions(yesOption: String, noOption: String) {
        this.displayedOptions.add("y")
        this.displayedOptions.add("n")
        this.options.add(yesOption)
        this.options.add(noOption)
    }

    fun command(line: (String) -> String) {
        this.line = line
    }

    fun value(value: Int?){
        this.value = value?.toString()
    }

    fun value(value: String?){
        this.value = value
    }

    fun useDefault(yes: Boolean = true){
        this.useDefault = yes
    }

    fun defaultValue(default: Int){
        this.useDefault = true
        this.defaultValue = default.toString()
    }

    fun defaultValue(default: String){
        this.useDefault = true
        this.defaultValue = default
    }

}

fun Player.respond(initializer: RequestResponseBuilder.() -> Unit, defaultBehavior: () -> Unit) {
    RequestResponseBuilder(defaultBehavior).apply(initializer).build()?.let { CommandParsers.setResponseRequest(this, it) }
}