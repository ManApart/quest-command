package core.commands

class ArgDelimiter(aliases: List<String>) {
    constructor(alias: String) : this(listOf(alias))

    private val aliases = aliases.map { it.lowercase() }
    val key = aliases.first()

    fun contains(word: String): Boolean {
        return aliases.any { word == it }
    }

    fun indexIn(args: List<String>): Int {
        aliases.forEach {
            val i = args.indexOf(it)
            if (i != -1) {
                return i
            }
        }
        return -1
    }
}

fun delims(vararg delimiters: String) = delimiters.map { ArgDelimiter(it) }
fun delims(delimiters: List<String>) = delimiters.map { ArgDelimiter(it) }

fun List<ArgDelimiter>.contains(string: String): Boolean {
    return any { it.contains(string) }
}
