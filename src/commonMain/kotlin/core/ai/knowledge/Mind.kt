package core.ai.knowledge

import core.ai.AI
import core.ai.DumbAI
import core.thing.Thing
import crafting.Recipe
import traveling.location.network.LocationNode

data class Mind(
    val ai: AI = DumbAI(),
    val memory: CreatureMemory = CreatureMemory()
) {
    lateinit var creature: Thing; private set

    fun updateCreature(creature: Thing) {
        this.creature = creature
        ai.creature = creature
    }

    fun knows(kind: String): ListFact? {
        return memory.getListFact(kind)
    }

    fun knows(source: Subject, kind: String): Fact? {
        return memory.getFact(source, kind)
    }

    fun knowsThingByKind(kind: String): Thing? {
        return memory.getSubjects(kind).firstNotNullOfOrNull { it.getThing() }
    }

    fun thingByKindExists(kind: String): Boolean {
        return knowsThingByKind(kind) != null
    }

    fun knowsLocationByKind(kind: String): LocationNode? {
        return memory.getSubjects(kind).firstNotNullOfOrNull { it.getLocation() }
    }

    fun locationByKindExists(kind: String): Boolean {
        return knowsLocationByKind(kind) != null
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
        val fact = knows("Location") ?: return false
        return fact.sources.contains(Subject(location))
    }

    fun knows(recipe: Recipe): Boolean {
        val fact = knows("Recipe") ?: return false
        return fact.sources.contains(Subject(recipe.name))
    }

    fun discover(location: LocationNode) {
        learn("Location", Subject(location))
    }

    fun discover(recipe: Recipe) {
        learn("Recipe", Subject(recipe.name))
    }

    fun setAggroTarget(enemy: Thing) {
        learn(Fact(Subject(enemy), "aggroTarget"))
    }

    fun getAggroTarget(): Thing? {
        return knowsThingByKind("aggroTarget")
    }

    fun clearAggroTarget() {
        getAggroTarget()?.let {
            memory.forget(Fact(Subject(it), "aggroTarget"))
        }
    }

    fun setUseTarget(enemy: Thing) {
        learn(Fact(Subject(enemy), "useTarget"))
    }

    fun getUseTarget(): Thing? {
        return knowsThingByKind("useTarget")
    }

    fun clearUseTarget() {
        getAggroTarget()?.let {
            memory.forget(Fact(Subject(it), "useTarget"))
        }
    }

}