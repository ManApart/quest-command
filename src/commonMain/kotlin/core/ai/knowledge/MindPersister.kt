package core.ai.knowledge

import core.ai.PlayerControlledAI
import core.properties.PropertiesP

@kotlinx.serialization.Serializable
data class MindP(
    val playerControlledAI: Boolean,
    val facts: List<FactP>,
    val listFacts: List<ListFactP>,
) {
    constructor(b: Mind) : this(b.ai is PlayerControlledAI, b.memory.getAllFacts().map { FactP(it) }, b.memory.getAllListFacts().map { ListFactP(it) })
}

@kotlinx.serialization.Serializable
data class FactP(val source: SimpleSubject, val kind: String, val props: PropertiesP) {
    constructor(b: Fact) : this(b.source, b.kind, PropertiesP(b.props))
    fun parsed(): Fact{
        return Fact(source, kind, props.parsed())
    }
}

@kotlinx.serialization.Serializable
data class ListFactP(val kind: String, val sources: List<SimpleSubject>, val props: PropertiesP) {
    constructor(b: ListFact) : this(b.kind, b.sources, PropertiesP(b.props))
    fun parsed(): ListFact{
        return ListFact(kind, sources, props.parsed())
    }
}