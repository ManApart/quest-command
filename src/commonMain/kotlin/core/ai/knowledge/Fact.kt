package core.ai.knowledge

import core.properties.Properties

private val unknownSubject = SimpleSubject()
val UNKNOWN_FACT = Fact(unknownSubject, "Unknown")
val UNKNOWN_LIST_FACT = ListFact("Unknown", unknownSubject)

enum class AffectionLevel(val amount: Int) { HATES(-10), DISLIKES(-5), NEUTRAL(0), LIKES(5), LOVES(10) }

fun Int.affection() = AffectionLevel.values().filter { it.amount < this }.maxOrNull()!!
fun Int.atLeast(level: AffectionLevel) = level.amount <= this
fun Int.atMost(level: AffectionLevel) = level.amount >= this

@kotlinx.serialization.Serializable
data class Fact(val source: SimpleSubject, val kind: String)

data class Fact2(val source: Subject, val kind: String, val props: Properties) //TODO

@kotlinx.serialization.Serializable
data class ListFact(val kind: String, val sources: List<SimpleSubject>) {
    constructor(kind: String, source: SimpleSubject): this(kind, listOf(source))
}