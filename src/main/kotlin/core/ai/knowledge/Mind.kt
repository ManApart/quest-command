package core.ai.knowledge

import core.thing.Thing

class Mind {
    lateinit var creature: Thing
    val personalFacts = mutableMapOf<String, List<Fact>>()
    val personalRelationships = mutableMapOf<String, List<Relationship>>()
    private val shortTermMemory = ShortTermMemory()

    fun knows(kind: String, relatesTo: Subject) = knows(Subject(creature), kind, relatesTo)
    fun knows(source: Subject, kind: String, relatesTo: Subject): Relationship {
        return KnowledgeManager.relationshipFinders
            .filter { it.matches(source, kind, relatesTo) }
            .map { it.findRelationship.invoke(this, source, kind, relatesTo) }
            .average()
    }

    fun knows(kind: String) = knows(Subject(creature), kind)
    fun knows(source: Subject, kind: String): Fact {
        return shortTermMemory.getFact(source, kind) ?: KnowledgeManager.factFinders
            .filter { it.matches(source, kind) }
            .map { it.findFact.invoke(this, source, kind) }
            .average()
            .also { shortTermMemory.remember(it) }
    }

    fun forgetShortTermMemory(){
        shortTermMemory.forget()
    }

}