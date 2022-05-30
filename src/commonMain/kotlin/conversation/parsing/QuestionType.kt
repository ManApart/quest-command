package conversation.parsing

enum class QuestionType {
    STATEMENT, HOW, WHAT, WHEN, WHERE, WHY, WILL
}

fun questionTypeFromWord(word: String): QuestionType? {
    val cleanWord = word.lowercase().trim()
    return QuestionType.values().firstOrNull { it.name.lowercase() == cleanWord }
}