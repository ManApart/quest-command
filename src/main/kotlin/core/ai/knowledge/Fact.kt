package core.ai.knowledge

private val unknownSubject = { _: Subject -> false }
val UNKNOWN_FACT = Fact(unknownSubject, "Unknown", 0, 0)
val UNKNOWN_RELATIONSHIP = Relationship(unknownSubject, "Unknown", unknownSubject, 0, 0)

enum class ConfidenceLevel(val amount: Int) { UNKNOWN(0), GUESS(25), ESTIMATE(50), CONFIDENT(75), POSITIVE(100) }

fun Int.confidence() = ConfidenceLevel.values().filter { it.amount < this }.maxOrNull()!!
fun Int.atLeast(level: ConfidenceLevel) = level.amount <= this
fun Int.atMost(level: ConfidenceLevel) = level.amount >= this

enum class AffectionLevel(val amount: Int) { HATES(-10), DISLIKES(-5), NEUTRAL(0), LIKES(5), LOVES(10) }

fun Int.affection() = AffectionLevel.values().filter { it.amount < this }.maxOrNull()!!
fun Int.atLeast(level: AffectionLevel) = level.amount <= this
fun Int.atMost(level: AffectionLevel) = level.amount >= this

data class Fact(val source: SubjectFilter, val kind: String, val confidence: Int, val amount: Int = 0) {
    operator fun plus(other: Fact): Fact {
        return Fact(source, kind, confidence + other.confidence, amount + other.amount)
    }
}

fun List<Fact>.sum(): Fact {
    if (this.isEmpty()) return UNKNOWN_FACT
    return Fact(first().source, first().kind, sumOf { it.confidence }, sumOf { it.amount })
}

fun List<Fact>.average(): Fact {
    if (this.isEmpty()) return UNKNOWN_FACT
    val summedConfidence = sumOf { it.confidence }
    return Fact(first().source, first().kind, summedConfidence / size, sumOf { it.amount * it.confidence } / summedConfidence)
}

data class Relationship(val source: SubjectFilter, val kind: String, val relatesTo: (Subject) -> Boolean, val confidence: Int, val amount: Int = 0)

fun List<Relationship>.sum(): Relationship {
    if (this.isEmpty()) return UNKNOWN_RELATIONSHIP
    return Relationship(first().source, first().kind, first().relatesTo, sumOf { it.confidence }, sumOf { it.amount })
}

fun List<Relationship>.average(): Relationship {
    if (this.isEmpty()) return UNKNOWN_RELATIONSHIP
    val summedConfidence = sumOf { it.confidence }
    return Relationship(first().source, first().kind, first().relatesTo, summedConfidence / size, sumOf { it.amount * it.confidence } / summedConfidence)
}