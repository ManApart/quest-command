package core.ai.knowledge

data class KnowledgeFinder(val factFinder: FactFinder? = null, val relationshipFinder: RelationshipFinder? = null)

class FactFinder(
    private val relevantKind: (String) -> Boolean = { true },
    private val relevantSources: List<(Subject) -> Boolean> = listOf { true },
    val findFact: ((mind: Mind, source: Subject, kind: String) -> Fact),
) {

    fun matches(source: Subject, kind: String): Boolean {
        return relevantKind(kind) && relevantSources.all { it(source) }
    }
}

class RelationshipFinder(
    private val relevantKind: (String) -> Boolean = { true },
    private val relevantSource: List<(Subject) -> Boolean> = listOf { true },
    private val relevantRelatesTo: List<(Subject) -> Boolean> = listOf { true },
    val findRelationship: ((mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship)
) {

    fun matches(source: Subject, kind: String, relatesTo: Subject): Boolean {
        return relevantKind(kind) && relevantSource.all { it(source) } && relevantRelatesTo.all { it(relatesTo) }
    }
}