package core.conditional

class ConditionalString(val name: String, val options: List<StringOption>) {
    fun getOption(): String {
        val option = options.firstOrNull { it.condition() }
        return option?.option ?: ""
    }
}