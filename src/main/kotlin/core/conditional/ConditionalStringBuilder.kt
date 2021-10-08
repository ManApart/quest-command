package core.conditional


class ConditionalStringBuilder(private val defaultOption: String? = null) {
    private var name: String = ""
    private val options = mutableListOf<StringOption>()

    fun build(bases: List<ConditionalStringBuilder> = listOf()): ConditionalString {
        val allOptions = options + bases.reversed().flatMap { it.options }
        return if (defaultOption != null && allOptions.isEmpty()) {
            ConditionalString(name, listOf(StringOption(defaultOption)))
        } else ConditionalString(name, allOptions)
    }

    /**
     * Options are order dependent. Options are evaluated in the order they are added
     */
    fun option(text: String, condition: () -> Boolean = { true }) {
        options.add(StringOption(text, condition))
    }

}

fun conditionalString(initializer: ConditionalStringBuilder.() -> Unit = {}): ConditionalStringBuilder {
    return ConditionalStringBuilder().apply(initializer)
}