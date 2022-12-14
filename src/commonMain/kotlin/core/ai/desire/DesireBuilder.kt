package core.ai.desire

import core.ai.AIManager.actions
import core.ai.action.AIAction
import core.ai.action.dsl.ActionRecipe
import core.conditional.Context
import core.conditional.ContextData
import core.thing.Thing
import core.utility.putAbsent
import crafting.result

class DesireBuilder(val condition: (Thing, Context) -> Boolean?, private val context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf()) {
    var priority: Int? = null
    private val depthScale: Int = 2
    private val children: MutableList<DesireBuilder> = mutableListOf()
    private val desires: MutableList<Pair<String, String>> = mutableListOf()


    fun cond(condition: (Thing, Context) -> Boolean? = { _, _ -> true }, initializer: DesireBuilder.() -> Unit) {
        children.add(DesireBuilder(condition, context.toMutableMap()).apply(initializer))
    }

    fun context(name: String, accessor: (Thing, Context) -> Any?) {
        context[name] = accessor
    }

    fun desire(name: String, agenda: String) {
        desires.add(Pair(name, agenda))
    }

    internal fun build(): DesireTree {
        return build(mapOf())
    }

    private fun build(parentContext: ContextData, depth: Int = 0): DesireTree {
        val usedPriority = priority ?: (10 + depthScale * depth)
        //This context overrides parent context
        parentContext.entries.forEach { (key, value) -> context.putAbsent(key, value) }
        val usedContext = Context(context)

        val usedDesires = desires.map { (name, agenda) ->
            Desire(name, usedPriority, agenda)
        }
        val usedChildren = children.map { it.build(context, depth + 1) }

        return DesireTree(condition, usedDesires, usedContext, usedChildren)
    }
}

fun desires(
    condition: (Thing, Context) -> Boolean? = { _, _ -> true },
    context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf(),
    initializer: DesireBuilder.() -> Unit
): List<DesireTree> {
    return listOf(DesireBuilder(condition, context).apply(initializer).build())
}
