package core.ai.knowledge

private val unknownSubject = SimpleSubject()
val UNKNOWN_FACT = Fact(unknownSubject, "Unknown", 0, 0)
val UNKNOWN_LIST_FACT = ListFact("Unknown", unknownSubject)
val UNKNOWN_RELATIONSHIP = Relationship(unknownSubject, "Unknown", unknownSubject, 0, 0)

enum class ConfidenceLevel(val amount: Int) { UNKNOWN(0), GUESS(25), ESTIMATE(50), CONFIDENT(75), POSITIVE(100) }

fun Int.confidence() = ConfidenceLevel.values().filter { it.amount < this }.maxOrNull()!!
fun Int.atLeast(level: ConfidenceLevel) = level.amount <= this
fun Int.atMost(level: ConfidenceLevel) = level.amount >= this

enum class AffectionLevel(val amount: Int) { HATES(-10), DISLIKES(-5), NEUTRAL(0), LIKES(5), LOVES(10) }

fun Int.affection() = AffectionLevel.values().filter { it.amount < this }.maxOrNull()!!
fun Int.atLeast(level: AffectionLevel) = level.amount <= this
fun Int.atMost(level: AffectionLevel) = level.amount >= this

@kotlinx.serialization.Serializable
data class Fact(val source: SimpleSubject, val kind: String, val confidence: Int, val amount: Int = 0) {
    operator fun plus(other: Fact): Fact {
        return Fact(source, kind, confidence + other.confidence, amount + other.amount)
    }

    fun confident(): Boolean {
        return confidence.atLeast(ConfidenceLevel.CONFIDENT)
    }
}

fun List<Fact>.sum(): Fact {
    if (this.isEmpty()) return UNKNOWN_FACT
    return Fact(first().source, first().kind, sumOf { it.confidence }, sumOf { it.amount })
}

fun List<Fact>.average(): Fact {
    if (this.isEmpty()) return UNKNOWN_FACT
    val summedConfidence = sumOf { it.confidence }.takeIf { it != 0 } ?: 1
    return Fact(first().source, first().kind, summedConfidence / size, sumOf { it.amount * it.confidence } / summedConfidence)
}

@kotlinx.serialization.Serializable
data class ListFact(val kind: String, val sources: List<SimpleSubject>) {
    constructor(kind: String, source: SimpleSubject): this(kind, listOf(source))
}

fun List<ListFact>.sum(): ListFact {
    if (this.isEmpty()) return UNKNOWN_LIST_FACT
    return ListFact(first().kind, flatMap { it.sources })
}

@kotlinx.serialization.Serializable
data class Relationship(val source: SimpleSubject, val kind: String, val relatesTo: SimpleSubject, val confidence: Int, val amount: Int = 0)

fun List<Relationship>.sum(): Relationship {
    if (this.isEmpty()) return UNKNOWN_RELATIONSHIP
    return Relationship(first().source, first().kind, first().relatesTo, sumOf { it.confidence }, sumOf { it.amount })
}

fun List<Relationship>.average(): Relationship {
    if (this.isEmpty()) return UNKNOWN_RELATIONSHIP
    val summedConfidence = sumOf { it.confidence }.takeIf { it != 0 } ?: 1
    return Relationship(first().source, first().kind, first().relatesTo, summedConfidence / size, sumOf { it.amount * it.confidence } / summedConfidence)
}