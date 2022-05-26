package core.ai.knowledge

@kotlinx.serialization.Serializable
data class MindP(
    val aiName: String,
    val personalFacts: List<FactP>,
    val personalRelationships: List<RelationshipP>,
) {
    constructor(b: Mind): this(b.ai.name, b.personalFacts.values.flatten().map { FactP(it) }, b.personalRelationships.values.flatten().map { RelationshipP(it) })

    fun parsedFacts(): MutableMap<String, MutableList<Fact>>{
        return personalFacts.map { it.parsed() }.groupBy { it.kind }.mapValues { it.value.toMutableList() }.toMutableMap()
    }

    fun parsedRelationships():  MutableMap<String, MutableList<Relationship>>{
        return personalRelationships.map { it.parsed() }.groupBy { it.kind }.mapValues { it.value.toMutableList() }.toMutableMap()
    }
}