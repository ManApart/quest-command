package core.conditional

data class StringOption(val option: String, val condition: () -> Boolean = { true })

data class ConditionalString(val options: List<StringOption> = listOf(StringOption(""))) {
    constructor(onlyOption: String) : this(listOf(StringOption(onlyOption)))

    fun getOption(): String {
        val option = options.firstOrNull { it.condition() }
        return option?.option ?: ""
    }
}