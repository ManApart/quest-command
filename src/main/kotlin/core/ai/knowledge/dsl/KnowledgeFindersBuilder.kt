package core.ai.knowledge.dsl

import core.ai.knowledge.KnowledgeFinder

class KnowledgeFindersBuilder {
    private val facts = mutableListOf<FactFinderBuilder>()
    private val relationships = mutableListOf<RelationshipFinderBuilder>()
    private val kindChildren = mutableListOf<KnowledgeFindersKindBuilder>()

    fun fact(kind: String, initializer: FactFinderBuilder.() -> Unit) {
        facts.add(FactFinderBuilder(kind).apply(initializer))
    }

    fun relationship(kind: String, initializer: RelationshipFinderBuilder.() -> Unit) {
        relationships.add(RelationshipFinderBuilder(kind).apply(initializer))
    }

    fun kind(kind: String, initializer: KnowledgeFindersKindBuilder.() -> Unit) {
        kindChildren.add(KnowledgeFindersKindBuilder(kind).apply(initializer))
    }

    fun build(): List<KnowledgeFinder> {
        return facts.map { KnowledgeFinder(it.build()) } +
                relationships.map { KnowledgeFinder(it.build()) } +
                kindChildren.flatMap { it.build() }
    }
}

//Used only to allow grouping by kind in the main builder
class KnowledgeFindersKindBuilder(private val kind: String) {
    private val facts = mutableListOf<FactFinderBuilder>()
    private val relationships = mutableListOf<RelationshipFinderBuilder>()

    fun fact(initializer: FactFinderBuilder.() -> Unit) {
        facts.add(FactFinderBuilder(kind).apply(initializer))
    }

    fun relationship(initializer: RelationshipFinderBuilder.() -> Unit) {
        relationships.add(RelationshipFinderBuilder(kind).apply(initializer))
    }

    fun build(): List<KnowledgeFinder> {
        return facts.map { KnowledgeFinder(it.build()) } + relationships.map { KnowledgeFinder(it.build()) }
    }
}

fun knowledgeFinders(initializer: KnowledgeFindersBuilder.() -> Unit): List<KnowledgeFinder> {
    return KnowledgeFindersBuilder().apply(initializer).build()
}