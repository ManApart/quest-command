package core.conditional

//TODO - do we still need a name?
class ConditionalString(val name: String, val options: List<StringOption> = listOf(StringOption(""))) {
    fun getOption(): String {
        val option = options.firstOrNull { it.condition() }
        return option?.option ?: ""
    }
}