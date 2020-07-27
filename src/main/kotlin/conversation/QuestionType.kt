package conversation

enum class QuestionType {
    HOW, WHAT, WHEN, WHERE, WHY, WILL
}

fun questionTypeFromWord(word: String): QuestionType? {
    val cleanWord = word.toLowerCase().trim()
    return QuestionType.values().firstOrNull { it.name.toLowerCase() == cleanWord }
}