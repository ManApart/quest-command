package core.ai.knowledge

import core.ai.PlayerControlledAI

@kotlinx.serialization.Serializable
data class MindP(
    val playerControlledAI: Boolean,
    val personalFacts: List<Fact>,
    val personalListFacts: List<ListFact>,
    val personalRelationships: List<Relationship>,
) {
    constructor(b: Mind): this(b.ai is PlayerControlledAI, b.memory.getAllFacts(), b.memory.getAllListFacts(), b.memory.getAllRelationships())
}