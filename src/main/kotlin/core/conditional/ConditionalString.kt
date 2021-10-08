package core.conditional

//TODO - do we still need a name?
data class ConditionalString(val name: String, val options: List<StringOption> = listOf(StringOption(""))) {
    constructor(onlyOption: String): this("", listOf(StringOption(onlyOption)))
    fun getOption(): String {
        val option = options.firstOrNull { it.condition() }
        return option?.option ?: ""
    }
}