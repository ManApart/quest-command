package conversation.parsing

enum class Verb(private vararg val synonyms: String = arrayOf()) {
    BE("are", "is", "am");

    fun matches(word: String): Boolean {
        return name.toLowerCase() == word || synonyms.contains(word)
    }
}

fun verbFromWord(word: String): Verb? {
    val cleanWord = word.toLowerCase().trim()
    return Verb.values().firstOrNull { it.matches(cleanWord) }
}