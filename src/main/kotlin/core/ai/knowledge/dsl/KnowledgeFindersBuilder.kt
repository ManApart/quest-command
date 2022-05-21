package core.ai.knowledge.dsl

import core.ai.knowledge.KnowledgeFinder

class KnowledgeFindersBuilder(private val kind: String? = null) {
    private val facts = mutableListOf<FactFinderBuilder>()
    private val relationships = mutableListOf<RelationshipFinderBuilder>()
    private val kindChildren = mutableListOf<KnowledgeFindersBuilder>()

    fun fact(initializer: FactFinderBuilder.() -> Unit) {
        val builder = if (kind != null) FactFinderBuilder().apply { kind(kind) } else FactFinderBuilder()
        facts.add(builder.apply(initializer))
    }

//
//    fun relationship(kind: String, initializer: RelationshipFinderBuilder.() -> Unit) {
//        relationships.add(RelationshipFinderBuilder(kind).apply(initializer))
//    }

    fun kind(kind: String, initializer: KnowledgeFindersBuilder.() -> Unit) {
        kindChildren.add(KnowledgeFindersBuilder(kind).apply(initializer))
    }

    fun build(): List<KnowledgeFinder> {
        return facts.map { KnowledgeFinder(it.build()) } +
                relationships.map { KnowledgeFinder(it.build()) } +
                kindChildren.flatMap { it.build() }
    }
}

fun knowledgeFinders(initializer: KnowledgeFindersBuilder.() -> Unit): List<KnowledgeFinder> {
    return KnowledgeFindersBuilder().apply(initializer).build()
}