package core.ai.knowledge.dsl

import core.ai.knowledge.*

data class Opinion(val confidence: Int = 0, val amount: Int = 0)

class KnowledgeFindersBuilder(
    private val kind: (String) -> Boolean = { true },
    private val source: (SubjectFilter)? = null,
    private val relatesTo: (SubjectFilter)? = null,
    private val comparison: (Subject, Subject) -> Boolean = { _, _ -> true }
) {
    private val facts = mutableListOf<((mind: Mind, source: Subject, kind: String) -> Fact)>()
    private val listFacts = mutableListOf<((mind: Mind, kind: String) -> ListFact)>()
    private val relationships = mutableListOf<((mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship)>()
    private val children = mutableListOf<KnowledgeFindersBuilder>()

    fun factFull(finder: (mind: Mind, source: Subject, kind: String) -> Fact) {
        facts.add(finder)
    }

    fun fact(finder: (mind: Mind, source: Subject, kind: String) -> Opinion) {
        facts.add { mind: Mind, source: Subject, kind: String ->
            val (confidence, amount) = finder(mind, source, kind)
            Fact(SimpleSubject(source), kind, confidence, amount)
        }
    }

    fun listFactFull(finder: (mind: Mind, kind: String) -> ListFact) {
        listFacts.add(finder)
    }

    fun listFact(finder: (mind: Mind, kind: String) -> List<String>) {
        listFacts.add { mind: Mind, kind: String ->
            val source = finder(mind, kind).map { SimpleSubject(it) }
            ListFact(kind, source)
        }
    }

    fun relationshipFull(finder: (mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship) {
        relationships.add(finder)
    }

    fun relationship(finder: (mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Opinion) {
        relationships.add { mind: Mind, source: Subject, kind: String, relatesTo: Subject ->
            val (confidence, amount) = finder(mind, source, kind, relatesTo)
            Relationship(SimpleSubject(source), kind, SimpleSubject(relatesTo), confidence, amount)
        }
    }

    fun kind(kind: String, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder({ kind.equals(it, ignoreCase = true) }).apply(initializer))
    }

    fun kind(kind: (String) -> Boolean, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder(kind).apply(initializer))
    }

    fun source(topic: String, initializer: KnowledgeFindersBuilder.() -> Unit) {
        this.source({ source -> source.topic.equals(topic, ignoreCase = true) }, initializer)
    }

    fun source(relevantSource: SubjectFilter, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder(kind, relevantSource).apply(initializer))
    }

    fun relatesTo(topic: String, initializer: KnowledgeFindersBuilder.() -> Unit) {
        this.relatesTo({ relatesTo -> relatesTo.topic.equals(topic, ignoreCase = true) }, initializer)
    }

    fun relatesTo(relatesTo: SubjectFilter, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder(kind, relatesTo = relatesTo).apply(initializer))
    }

    fun compare(comparison: (Subject, Subject) -> Boolean, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder(kind, comparison = comparison).apply(initializer))
    }

    fun build(
        parentSources: List<(SubjectFilter)> = emptyList(),
        parentRelatesTo: List<(SubjectFilter)> = emptyList(),
        parentComparison: List<((Subject, Subject) -> Boolean)> = emptyList(),
    ): KnowledgeFinderTree {
        val sources = (parentSources + listOfNotNull(source))
        val relatesTo = (parentRelatesTo + listOfNotNull(relatesTo))
        val comparison = (parentComparison + listOfNotNull(comparison))

        return KnowledgeFinderTree(kind, sources, relatesTo, comparison, children.map { it.build(sources, relatesTo) }, facts, listFacts, relationships)
    }
}

fun knowledgeFinders(initializer: KnowledgeFindersBuilder.() -> Unit): List<KnowledgeFinderTree> {
    return listOf(KnowledgeFindersBuilder().apply(initializer).build())
}