package core.ai.knowledge

@kotlinx.serialization.Serializable
data class MindP(
    val aiName: String,
    val personalFacts: List<Fact>,
    val personalRelationships: List<Relationship>,
) {
    constructor(b: Mind): this(b.ai.name, b.memory.getAllFacts(), b.memory.getAllRelationships())
}