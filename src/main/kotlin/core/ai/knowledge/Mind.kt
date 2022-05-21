package core.ai.knowledge

import core.thing.Thing

class Mind {
    lateinit var creature: Thing
    private val personalFacts = mutableMapOf<String, List<Fact>>()
    private val personalRelationships = mutableMapOf<String, List<Relationship>>()

    fun knows(kind: String, relatesTo: Subject) = knows(Subject(creature), kind, relatesTo)
    fun knows(source: Subject, kind: String, relatesTo: Subject): Relationship {
        val foundFacts = KnowledgeManager.relationshipFinders.filter { it.matches(source, kind, relatesTo) }.map { it.findRelationship.invoke(this, source, kind, relatesTo) }
        return Relationship(source, kind, relatesTo, foundFacts.sumOf { it.confidence }, foundFacts.sumOf { it.amount })
    }

    fun knows(kind: String) = knows(Subject(creature), kind)
    fun knows(source: Subject, kind: String): Fact {
        val foundFacts = KnowledgeManager.factFinders.filter { it.matches(source, kind) }.map { it.findFact.invoke(this, source, kind) }
        return Fact(source, kind, foundFacts.sumOf { it.confidence }, foundFacts.sumOf { it.amount })
    }

}