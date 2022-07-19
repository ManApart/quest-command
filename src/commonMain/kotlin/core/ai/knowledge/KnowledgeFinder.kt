package core.ai.knowledge

data class KnowledgeFinderTree(
    private val relevantKind: (String) -> Boolean = { true },
    private val relevantSource: List<(Subject) -> Boolean> = listOf({ true }),
    private val relevantRelatesTo: List<(Subject) -> Boolean> = listOf({ true }),
    private val children: List<KnowledgeFinderTree> = listOf(),
    private val factFinders: List<(mind: Mind, source: Subject, kind: String) -> Fact> = emptyList(),
    private val listFactFinders: List<(mind: Mind, kind: String) -> ListFact> = emptyList(),
    private val relationshipFinders: List<(mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship> = emptyList()
) {
    fun getFacts(mind: Mind, source: Subject, kind: String): List<Fact> {
        return if (relevantKind(kind) && relevantSource.all { it(source) }){
            factFinders.map { it(mind, source, kind) } + children.flatMap { it.getFacts(mind, source, kind) }
        } else listOf()
    }

    fun getListFacts(mind: Mind, kind: String): List<ListFact> {
        return if (relevantKind(kind)){
            listFactFinders.map { it(mind, kind) } + children.flatMap { it.getListFacts(mind, kind) }
        } else listOf()
    }

    fun getRelationships(mind: Mind, source: Subject, kind: String, relatesTo: Subject): List<Relationship> {
        return if (relevantKind(kind) && relevantSource.all { it(source) } && relevantRelatesTo.all { it(relatesTo) }){
            relationshipFinders.map { it(mind, source, kind, relatesTo) } + children.flatMap { it.getRelationships(mind, source, kind, relatesTo) }
        } else listOf()
    }
}