package core.ai.knowledge

import core.ai.AI
import core.ai.DumbAI
import core.thing.Thing

data class Mind(
    val ai: AI = DumbAI(),
    val memory: CreatureMemory = CreatureMemory()
){
    private val shortTermMemory = CreatureMemory()
    lateinit var creature: Thing; private set

    fun updateCreature(creature: Thing){
        this.creature = creature
        ai.creature = creature
    }

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
    fun learn(source: SimpleSubject, kind: String, confidenceIncrease: Int, amountIncrease: Int) {
        val existing = memory.getFact(source, kind)
        val confidence = (existing?.confidence ?: 0) + confidenceIncrease
        val amount = (existing?.confidence ?: 0) + amountIncrease
        memory.remember(Fact(source, kind, confidence, amount))
    }

    fun learn(relationship: Relationship) = learn(relationship.source, relationship.kind, relationship.relatesTo, relationship.confidence, relationship.amount)
    fun learn(source: SimpleSubject, kind: String, relatesTo: SimpleSubject, confidenceIncrease: Int, amountIncrease: Int) {
        val existing = memory.getRelationship(source, kind, relatesTo)
        val confidence = (existing?.confidence ?: 0) + confidenceIncrease
        val amount = (existing?.confidence ?: 0) + amountIncrease
        memory.remember(Relationship(source, kind, relatesTo, confidence, amount))
    }

    fun forgetShortTermMemory() {
        shortTermMemory.forget()
    }

}