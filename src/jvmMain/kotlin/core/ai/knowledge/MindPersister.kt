package core.ai.knowledge

@kotlinx.serialization.Serializable
data class MindP(
    val aiName: String,
    val personalFacts: List<Fact>,
    val personalListFacts: List<ListFact>,
    val personalRelationships: List<Relationship>,
) {
    constructor(b: Mind): this(b.ai.name, b.memory.getAllFacts(), b.memory.getAllListFacts(), b.memory.getAllRelationships())
}