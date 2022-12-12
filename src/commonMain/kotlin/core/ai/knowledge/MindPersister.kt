package core.ai.knowledge

import core.ai.PlayerControlledAI

@kotlinx.serialization.Serializable
data class MindP(
    val playerControlledAI: Boolean,
    val facts: List<Fact>,
    val listFacts: List<ListFact>,
) {
    constructor(b: Mind): this(b.ai is PlayerControlledAI, b.memory.getAllFacts(), b.memory.getAllListFacts())
}