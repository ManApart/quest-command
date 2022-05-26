package core.ai.knowledge

// TODO - only allow persisting named subject filters?

@kotlinx.serialization.Serializable
data class FactP(
    val sourceFilter: String,
    val kind: String,
    val confidence: Int,
    val amount: Int,
) {
    constructor(b: Fact): this(SubjectP(b.source), b.kind, b.confidence, b.amount)

    fun parsed(): Fact{
        return Fact(source.parsed(), kind, confidence, amount)
    }
}

@kotlinx.serialization.Serializable
data class RelationshipP(
    val source: SubjectP,
    val kind: String,
    val relatesTo: SubjectP,
    val confidence: Int,
    val amount: Int,
) {
    constructor(b: Relationship): this(SubjectP(b.source), b.kind, SubjectP(b.relatesTo), b.confidence, b.amount)

    fun parsed(): Relationship{
        return Relationship(source.parsed(), kind, relatesTo.parsed(), confidence, amount)
    }
}