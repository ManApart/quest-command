package core.ai.knowledge

data class KnowledgeFinder(val factFinder: FactFinder? = null, val relationshipFinder: RelationshipFinder? = null)

class FactFinder(
    private val kind: String,
    private val relevantSource: (Subject) -> Boolean = { true },
    val findFact: ((mind: Mind, source: Subject, kind: String) -> Fact),
) {

    fun matches(source: Subject, kind: String): Boolean {
        return this.kind.equals(kind, ignoreCase = true) && relevantSource(source)
    }
}

class RelationshipFinder(
    private val kind: String,
    private val relevantSource: (Subject) -> Boolean = { true },
    private val relevantRelatesTo: (Subject) -> Boolean = { true },
    val findRelationship: ((mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship)
) {

    fun matches(source: Subject, kind: String, relatesTo: Subject): Boolean {
        return this.kind.equals(kind, ignoreCase = true) && relevantSource(source) && relevantRelatesTo(relatesTo)
    }
}