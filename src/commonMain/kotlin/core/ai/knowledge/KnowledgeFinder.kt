package core.ai.knowledge

data class KnowledgeFinderTree(
    private val relevantKind: (String) -> Boolean = { true },
    private val relevantSource: List<(Subject) -> Boolean> = listOf({ true }),
    private val relevantRelatesTo: List<(Subject) -> Boolean> = listOf({ true }),
    private val children: List<KnowledgeFinderTree> = listOf(),
    private val factFinders: List<FactFinder> = emptyList(),
    private val listFactFinders: List<ListFactFinder> = emptyList(),
    private val relationshipFinders: List<RelationshipFinder> = emptyList()
) {
    fun getFacts(mind: Mind, source: Subject, kind: String): List<Fact> {
        return if (relevantKind(kind) && relevantSource.all { it(source) }){
            factFinders.map { it.findFact(mind, source, kind) } + children.flatMap { it.getFacts(mind, source, kind) }
        } else listOf()
    }

    fun getListFacts(mind: Mind, source: Subject, kind: String): List<ListFact> {
        return if (relevantKind(kind)){
            listFactFinders.map { it.findFact(mind, kind) } + children.flatMap { it.getListFacts(mind, source, kind) }
        } else listOf()
    }

    fun getRelationships(mind: Mind, source: Subject, kind: String, relatesTo: Subject): List<Relationship> {
        return if (relevantKind(kind) && relevantSource.all { it(source) } && relevantRelatesTo.all { it(relatesTo) }){
            relationshipFinders.map { it.findRelationship(mind, source, kind, relatesTo) } + children.flatMap { it.getRelationships(mind, source, kind, relatesTo) }
        } else listOf()
    }
}

class FactFinder(
    private val relevantKind: (String) -> Boolean = { true },
    private val relevantSources: List<(Subject) -> Boolean> = listOf({ true }),
    val findFact: ((mind: Mind, source: Subject, kind: String) -> Fact),
) {

    fun matches(source: Subject, kind: String): Boolean {
        return relevantKind(kind) && relevantSources.all { it(source) }
    }
}

class ListFactFinder(
    private val relevantKind: (String) -> Boolean = { true },
    val findFact: ((mind: Mind, kind: String) -> ListFact),
) {

    fun matches(kind: String): Boolean {
        return relevantKind(kind)
    }
}

class RelationshipFinder(
    private val relevantKind: (String) -> Boolean = { true },
    private val relevantSource: List<(Subject) -> Boolean> = listOf({ true }),
    private val relevantRelatesTo: List<(Subject) -> Boolean> = listOf({ true }),
    val findRelationship: ((mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship)
) {

    fun matches(source: Subject, kind: String, relatesTo: Subject): Boolean {
        return relevantKind(kind) && relevantSource.all { it(source) } && relevantRelatesTo.all { it(relatesTo) }
    }
}