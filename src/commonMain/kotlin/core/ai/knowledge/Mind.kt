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
    lateinit var creature: Thing; private set

    fun updateCreature(creature: Thing){
        this.creature = creature
        ai.creature = creature
    }

    fun knows(kind: String): ListFact {
        return memory.getListFact(kind) ?: UNKNOWN_LIST_FACT //TODO - ok to return this?
    }

    fun knows(source: Subject, kind: String): Fact {
        return memory.getFact(source, kind) ?: UNKNOWN_FACT //TODO
    }

    fun learn(source: Subject, kind: String) {
        val fact = memory.getFact(source, kind) ?: Fact(source, kind)
        learn(fact)
    }

    fun learn(kind: String, addition: Subject) = learn(kind, listOf(addition))
    fun learn(kind: String, additions: List<Subject>) {
        val existing = memory.getListFact(kind)
        val fact = ListFact(kind, additions.toList() + (existing?.sources ?: listOf()))
        memory.remember(fact)
    }

    fun learn(fact: Fact) {
        memory.remember(fact)
    }

    fun learn(fact: ListFact) {
        memory.remember(fact)
    }

    fun forgetLongTermMemory() {
        memory.forget()
    }

    fun knows(location: LocationNode): Boolean {
        val fact = knows("Location")
        return fact.sources.contains(Subject(location))
    }

    fun knows(recipe: Recipe): Boolean {
        val fact = knows("Recipe")
        return fact.sources.contains(Subject(recipe.name))
    }

    fun discover(location: LocationNode){
        learn("Location", Subject(location))
    }

    fun discover(recipe: Recipe){
        learn("Recipe", Subject(recipe.name))
    }

}