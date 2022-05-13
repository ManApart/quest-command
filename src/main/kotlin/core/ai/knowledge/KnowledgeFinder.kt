package core.ai.knowledge

class KnowledgeFinder(
    private val kind: String,
    private val relevantSource: (Subject) -> Boolean = { true },
    private val relevantRelatesTo: (Subject) -> Boolean = { true },
    val findFact: ((mind: Mind, source: Subject, kind: String) -> Fact)? = null,
    val findRelationship: ((mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship)? = null
) {
    init {
        require(findFact != null || findRelationship != null) { "Find fact or find relationship must not be null!" }
    }

    fun matches(source: Subject, kind: String, relatesTo: Subject): Boolean {
        return this.kind.equals(kind, ignoreCase = true) && relevantSource(source) && relevantRelatesTo(relatesTo)
    }

    fun matches(source: Subject, kind: String): Boolean {
        return this.kind.equals(kind, ignoreCase = true) && relevantSource(source)
    }
}