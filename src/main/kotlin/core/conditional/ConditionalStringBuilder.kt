package core.conditional


class ConditionalStringBuilder {
    private var name: String = ""
    private val options = mutableListOf<StringOption>()

    fun build(): ConditionalString {
        return ConditionalString(name, options.toList())
    }

    /**
     * Options are order dependent. Options are evaluated in the order they are added
     */
    fun option(text: String, condition: () -> Boolean = { true }) {
        options.add(StringOption(text, condition))
    }

}

fun conditionalString(initializer: ConditionalStringBuilder.() -> Unit): ConditionalStringBuilder {
    return ConditionalStringBuilder().apply(initializer)
}