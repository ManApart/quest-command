package core.ai.knowledge.dsl

import core.ai.knowledge.*

class KnowledgeFindersBuilder(private val kind: (String) -> Boolean = { true }, private val source: ((Subject) -> Boolean)? = null, private val relatesTo: ((Subject) -> Boolean)? = null) {
    private val facts = mutableListOf<((mind: Mind, source: Subject, kind: String) -> Fact)>()
    private val relationships = mutableListOf<RelationshipFinderBuilder>()
    private val children = mutableListOf<KnowledgeFindersBuilder>()

    fun fact(finder: (mind: Mind, source: Subject, kind: String) -> Fact) {
        facts.add(finder)
    }

//
//    fun relationship(kind: String, initializer: RelationshipFinderBuilder.() -> Unit) {
//        relationships.add(RelationshipFinderBuilder(kind).apply(initializer))
//    }

    fun kind(kind: String, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder({ kind.equals(it, ignoreCase = true) }).apply(initializer))
    }

    fun kind(kind: (String) -> Boolean, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder(kind).apply(initializer))
    }

    fun source(name: String, initializer: KnowledgeFindersBuilder.() -> Unit) {
        this.source({ source -> source.name.equals(name, ignoreCase = true) }, initializer)
    }

    fun source(relevantSource: (Subject) -> Boolean, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder(kind, relevantSource).apply(initializer))
    }

    fun relatesTo(name: String, initializer: KnowledgeFindersBuilder.() -> Unit) {
        this.relatesTo({ relatesTo -> relatesTo.name.equals(name, ignoreCase = true) }, initializer)
    }

    fun relatesTo(relatesTo: (Subject) -> Boolean, initializer: KnowledgeFindersBuilder.() -> Unit) {
        children.add(KnowledgeFindersBuilder(kind, relatesTo = relatesTo).apply(initializer))
    }

    fun build(parentSources: List<((Subject) -> Boolean)> = emptyList(), parentRelatesTo: List<((Subject) -> Boolean)> = emptyList()): List<KnowledgeFinder> {
        val sources = (parentSources + listOfNotNull(source))
        val relatesTo = (parentRelatesTo + listOfNotNull(relatesTo))

        return facts.map { KnowledgeFinder(FactFinder(kind, sources, it)) } +
//                relationships.map { KnowledgeFinder(RelationshipFinder()) } +
                children.flatMap { it.build(sources, relatesTo) }
    }
}

fun knowledgeFinders(initializer: KnowledgeFindersBuilder.() -> Unit): List<KnowledgeFinder> {
    return KnowledgeFindersBuilder().apply(initializer).build()
}