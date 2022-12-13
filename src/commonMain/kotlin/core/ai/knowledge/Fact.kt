package core.ai.knowledge

import core.properties.Properties

private val unknownSubject = Subject(topic = "Unknown")
val UNKNOWN_FACT = Fact(unknownSubject, "Unknown")
val UNKNOWN_LIST_FACT = ListFact("Unknown", unknownSubject)

enum class AffectionLevel(val amount: Int) { HATES(-10), DISLIKES(-5), NEUTRAL(0), LIKES(5), LOVES(10) }

fun Int.affection() = AffectionLevel.values().filter { it.amount < this }.maxOrNull()!!
fun Int.atLeast(level: AffectionLevel) = level.amount <= this
fun Int.atMost(level: AffectionLevel) = level.amount >= this

data class Fact(val source: Subject, val kind: String, val props: Properties = Properties())

data class ListFact(val kind: String, val sources: List<Subject>, val props: Properties = Properties()) {
    constructor(kind: String, source: Subject, props: Properties = Properties()): this(kind, listOf(source), props)
}