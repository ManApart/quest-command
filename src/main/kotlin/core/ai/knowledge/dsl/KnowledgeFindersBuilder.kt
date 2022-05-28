package core.ai.knowledge.dsl

import core.ai.knowledge.*

class KnowledgeFindersBuilder(private val kind: (String) -> Boolean = { true }, private val source: (SubjectFilter)? = null, private val relatesTo: (SubjectFilter)? = null) {
    private val facts = mutableListOf<((mind: Mind, source: Subject, kind: String) -> Fact)>()
    private val relationships = mutableListOf<((mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship)>()
    private val children = mutableListOf<KnowledgeFindersBuilder>()

    fun factFull(finder: (mind: Mind, source: Subject, kind: String) -> Fact) {
        facts.add(finder)
    }

    fun fact(finder: (mind: Mind, source: Subject, kind: String) -> Pair<Int, Int>) {
        facts.add { mind: Mind, source: Subject, kind: String ->
            val (confidence, amount) = finder(mind, source, kind)
            Fact(SimpleSubject(source), kind, confidence, amount)
        }
    }

    fun relationshipFull(finder: (mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Relationship) {
        relationships.add(finder)
    }

    fun relationship(finder: (mind: Mind, source: Subject, kind: String, relatesTo: Subject) -> Pair<Int, Int>) {
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

    fun build(parentSources: List<(SubjectFilter)> = emptyList(), parentRelatesTo: List<(SubjectFilter)> = emptyList()): List<KnowledgeFinder> {
        val sources = (parentSources + listOfNotNull(source))
        val relatesTo = (parentRelatesTo + listOfNotNull(relatesTo))

        return facts.map { KnowledgeFinder(FactFinder(kind, sources, it)) } +
                relationships.map { KnowledgeFinder(relationshipFinder = RelationshipFinder(kind, sources, relatesTo, it)) } +
                children.flatMap { it.build(sources, relatesTo) }
    }
}

fun knowledgeFinders(initializer: KnowledgeFindersBuilder.() -> Unit): List<KnowledgeFinder> {
    return KnowledgeFindersBuilder().apply(initializer).build()
}