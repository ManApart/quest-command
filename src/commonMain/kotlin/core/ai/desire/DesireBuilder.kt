package core.ai.desire

import core.thing.Thing
import core.utility.RandomManager
import core.utility.applySuspending

class DesireBuilder(val condition: suspend (Thing) -> Boolean?) {
    //Set priority to an exact amount
    var priority: Int? = null
    //Give some additional priority to actions at the top level of this branch. Not recursive
    var additionalPriority: Int = 0
    private val depthScale: Int = 2
    private val children: MutableList<DesireBuilder> = mutableListOf()
    private val agendas: MutableList<String> = mutableListOf()


    suspend fun cond(condition: suspend (Thing) -> Boolean? = { _ -> true }, initializer: suspend DesireBuilder.() -> Unit) {
        children.add(DesireBuilder(condition).applySuspending(initializer))
    }

    suspend fun cond(randomChance: Int, condition: suspend (Thing) -> Boolean? = { _ -> true }, initializer: DesireBuilder.() -> Unit) {
        val newCondition: suspend (Thing) -> Boolean? = { thing ->  if (RandomManager.isSuccess(randomChance)) condition(thing) else null }
        children.add(DesireBuilder(newCondition).apply(initializer))
    }

    suspend fun tag(tag: String, initializer: suspend DesireBuilder.() -> Unit) {
        val b = DesireBuilder { source -> source.properties.tags.has(tag) }
        b.initializer()
        children.add(b)
    }

    fun agenda(agenda: String) {
        agendas.add(agenda)
    }

    internal fun build(depth: Int = 0): DesireTree {
        val usedPriority = priority ?: (10 + depthScale * depth + additionalPriority)
        val usedAgendas = agendas.map { Pair(it, usedPriority) }
        val usedChildren = children.map { it.build(depth + 1) }

        return DesireTree(condition, usedAgendas, usedChildren)
    }
}

suspend fun desires(
    condition: (Thing) -> Boolean? = { _ -> true },
    initializer: suspend DesireBuilder.() -> Unit
): List<DesireTree> {
    return listOf(DesireBuilder(condition).applySuspending(initializer).build())
}
