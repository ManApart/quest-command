package core.ai.knowledge

import core.ai.AI
import core.ai.DumbAI
import core.thing.Thing
import crafting.Recipe
import traveling.location.network.LocationNode

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

    fun knows(kind: String): ListFact {
        return shortTermMemory.getListFact(kind) ?: KnowledgeManager.listFactFinders
            .filter { it.matches( kind) }
            .map { it.findFact.invoke(this, kind) }
            .sum()
            .also { shortTermMemory.remember(it) }
    }

    fun knows(source: Subject, kind: String): Fact {
        return shortTermMemory.getFact(source, kind) ?: KnowledgeManager.factFinders
            .filter { it.matches(source, kind) }
            .map { it.findFact.invoke(this, source, kind) }
            .average()
            .also { shortTermMemory.remember(it) }
    }

    fun knows(source: Subject, kind: String, relatesTo: Subject): Relationship {
        return KnowledgeManager.relationshipFinders
            .filter { it.matches(source, kind, relatesTo) }
            .map { it.findRelationship.invoke(this, source, kind, relatesTo) }
            .average()
            .also { shortTermMemory.remember(it) }
    }

    fun learn(fact: Fact) = learn(fact.source, fact.kind, fact.confidence, fact.amount)
    fun learn(source: SimpleSubject, kind: String, confidenceIncrease: Int, amountIncrease: Int) {
        val existing = memory.getFact(source, kind)
        val confidence = (existing?.confidence ?: 0) + confidenceIncrease
        val amount = (existing?.confidence ?: 0) + amountIncrease
        val fact = Fact(source, kind, confidence, amount)
        memory.remember(fact)
        shortTermMemory.forget(fact)
    }

    fun learn(kind: String, addition: SimpleSubject) = learn(kind, listOf(addition))
    fun learn(kind: String, additions: List<SimpleSubject>) {
        val existing = memory.getListFact(kind)
        val fact = ListFact(kind, additions.toList() + (existing?.sources ?: listOf()))
        memory.remember(fact)
        shortTermMemory.forget(fact)
    }

    fun learn(relationship: Relationship) = learn(relationship.source, relationship.kind, relationship.relatesTo, relationship.confidence, relationship.amount)
    fun learn(source: SimpleSubject, kind: String, relatesTo: SimpleSubject, confidenceIncrease: Int, amountIncrease: Int) {
        val existing = memory.getRelationship(source, kind, relatesTo)
        val confidence = (existing?.confidence ?: 0) + confidenceIncrease
        val amount = (existing?.confidence ?: 0) + amountIncrease
        val relationship = Relationship(source, kind, relatesTo, confidence, amount)
        memory.remember(relationship)
        shortTermMemory.forget(relationship)
    }

    fun forgetShortTermMemory() {
        shortTermMemory.forget()
    }

    fun forgetLongTermMemory() {
        memory.forget()
    }

    fun knows(location: LocationNode): Boolean {
        val fact = knows("Location")
        return fact.sources.contains(SimpleSubject(location))
    }

    fun knows(recipe: Recipe): Boolean {
        val fact = knows("Recipe")
        return fact.sources.contains(SimpleSubject(recipe.name))
    }

    fun discover(location: LocationNode){
        learn("Location", SimpleSubject(location))
    }

    fun discover(recipe: Recipe){
        learn("Recipe", SimpleSubject(recipe.name))
    }

}