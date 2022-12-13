package core.ai.knowledge

enum class AffectionLevel(val amount: Int) { HATES(-10), DISLIKES(-5), NEUTRAL(0), LIKES(5), LOVES(10) }

fun Int.affection() = AffectionLevel.values().filter { it.amount < this }.maxOrNull()!!
fun Int.atLeast(level: AffectionLevel) = level.amount <= this
fun Int.atMost(level: AffectionLevel) = level.amount >= this