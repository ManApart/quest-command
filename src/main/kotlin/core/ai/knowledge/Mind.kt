package core.ai.knowledge

import core.thing.Thing

//TODO - move to some mind manager or something
val finders: List<KnowledgeFinder> = listOf()

class Mind {
    lateinit var creature: Thing
    private val personalFacts = mutableMapOf<String, List<Fact>>()
    private val personalRelationships = mutableMapOf<String, List<Relationship>>()

    fun knows(kind: String, relatesTo: Subject) = knows(Subject(creature), kind, relatesTo)
    fun knows(source: Subject, kind: String, relatesTo: Subject): Relationship {
        val foundFacts = finders.filter { it.matches(source, kind, relatesTo) }.mapNotNull { it.findRelationship?.invoke(this, source, kind, relatesTo) }
        return Relationship(source, kind, relatesTo, foundFacts.sumOf { it.confidence }, foundFacts.sumOf { it.amount })
    }

    fun knowsFact(kind: String) = knowsFact(Subject(creature), kind)
    fun knowsFact(source: Subject, kind: String): Fact {
        val foundFacts = finders.filter { it.matches(source, kind) }.mapNotNull { it.findFact?.invoke(this, source, kind) }
        return Fact(source, kind, foundFacts.sumOf { it.confidence }, foundFacts.sumOf { it.amount })
    }

}