package core.ai.knowledge

import core.ai.PlayerControlledAI
import core.properties.PropertiesP

@kotlinx.serialization.Serializable
data class MindP(
    val playerControlledAI: Boolean = false,
    val facts: List<FactP> = emptyList(),
    val listFacts: List<ListFactP> = emptyList(),
) {
    constructor(b: Mind) : this(b.ai is PlayerControlledAI, b.memory.getAllFacts().map { FactP(it) }, b.memory.getAllListFacts().map { ListFactP(it) })
}

@kotlinx.serialization.Serializable
data class FactP(val source: Subject, val kind: String, val props: PropertiesP = PropertiesP()) {
    constructor(b: Fact) : this(b.source, b.kind, PropertiesP(b.props))

    fun parsed(): Fact {
        return Fact(source, kind, props.parsed())
    }
}

@kotlinx.serialization.Serializable
data class ListFactP(val kind: String, val sources: List<Subject> = emptyList(), val props: PropertiesP = PropertiesP()) {
    constructor(b: ListFact) : this(b.kind, b.sources, PropertiesP(b.props))

    fun parsed(): ListFact {
        return ListFact(kind, sources, props.parsed())
    }
}
