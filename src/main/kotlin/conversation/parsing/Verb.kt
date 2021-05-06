package conversation.parsing

enum class Verb(private vararg val synonyms: String = arrayOf()) {
    BE("are", "is", "am");

    fun matches(word: String): Boolean {
        return name.lowercase() == word || synonyms.contains(word)
    }
}

fun verbFromWord(word: String): Verb? {
    val cleanWord = word.lowercase().trim()
    return Verb.values().firstOrNull { it.matches(cleanWord) }
}