package core.ai.knowledge

import core.thing.Thing

class Mind {
    lateinit var creature: Thing
    val personalFacts = mutableMapOf<String, MutableList<Fact>>()
    val personalRelationships = mutableMapOf<String, MutableList<Relationship>>()
    private val shortTermMemory = ShortTermMemory()

    fun knows(kind: String, relatesTo: Subject) = knows(Subject(creature), kind, relatesTo)
    fun knows(source: Subject, kind: String, relatesTo: Subject): Relationship {
        return KnowledgeManager.relationshipFinders
            .filter { it.matches(source, kind, relatesTo) }
            .map { it.findRelationship.invoke(this, source, kind, relatesTo) }
            .average()
            .also { shortTermMemory.remember(it) }
    }

    fun knows(kind: String) = knows(Subject(creature), kind)
    fun knows(source: Subject, kind: String): Fact {
        return shortTermMemory.getFact(source, kind) ?: KnowledgeManager.factFinders
            .filter { it.matches(source, kind) }
            .map { it.findFact.invoke(this, source, kind) }
            .average()
            .also { shortTermMemory.remember(it) }
    }

    fun learn(fact: Fact) = learn(fact.source, fact.kind, fact.confidence, fact.amount)
    fun learn(source: Subject, kind: String, confidenceIncrease: Int, amountIncrease: Int) {
        val existing = personalFacts[kind]?.firstOrNull { it.source == source }
        val confidence = (existing?.confidence ?: 0) + amountIncrease
        val amount = (existing?.confidence ?: 0) + confidenceIncrease
        personalFacts.putIfAbsent(kind, mutableListOf())
        personalFacts[kind]?.remove(existing)
        personalFacts[kind]?.add(Fact(source, kind, confidence, amount))
    }

    fun learn(relationship: Relationship) = learn(relationship.source, relationship.kind, relationship.relatesTo, relationship.confidence, relationship.amount)
    fun learn(source: Subject, kind: String, relatesTo: Subject, confidenceIncrease: Int, amountIncrease: Int) {
        val existing = personalRelationships[kind]?.firstOrNull { it.source == source }
        val confidence = (existing?.confidence ?: 0) + amountIncrease
        val amount = (existing?.confidence ?: 0) + confidenceIncrease
        personalRelationships.putIfAbsent(kind, mutableListOf())
        personalRelationships[kind]?.remove(existing)
        personalRelationships[kind]?.add(Relationship(source, kind, relatesTo, confidence, amount))
    }

    fun forgetShortTermMemory() {
        shortTermMemory.forget()
    }

}