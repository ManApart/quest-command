package core.ai.action.dsl

import core.ai.action.AIAction
import core.ai.action.AIActionTree
import core.conditional.Context
import core.conditional.ContextData
import core.events.Event
import core.thing.Thing
import core.utility.putAbsent

class AIActionBuilder(val condition: (Thing, Context) -> Boolean?, private val context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf()) {
    var priority: Int? = null
    private val depthScale: Int = 2
    private val children: MutableList<AIActionBuilder> = mutableListOf()
    private val actionRecipes: MutableList<ActionRecipe> = mutableListOf()


    fun cond(condition: (Thing, Context) -> Boolean? = { _, _ -> true }, initializer: AIActionBuilder.() -> Unit) {
        children.add(AIActionBuilder(condition, context.toMutableMap()).apply(initializer))
    }

    fun context(name: String, accessor: (Thing, Context) -> Any?) {
        context[name] = accessor
    }

    fun action(name: String, result: ((Thing, Context) -> Event)) {
        this.actionRecipes.add(ActionRecipe(name) { thing, context -> listOf(result(thing, context)) })
    }

    fun actions(name: String, results: ((Thing, Context) -> List<Event>)) {
        this.actionRecipes.add(ActionRecipe(name, results))
    }

    internal fun build(): AIActionTree {
        return build(mapOf())
    }

    private fun build(parentContext: ContextData, depth: Int = 0): AIActionTree {
        val usedPriority = priority ?: (10 + depthScale * depth)
        //This context overrides parent context
        parentContext.entries.forEach { (key, value) -> context.putAbsent(key, value) }
        val usedContext = Context(context)

        val actions = actionRecipes.map { protoAction ->
            AIAction(protoAction.name, usedContext, protoAction.createEvents, usedPriority)
        }
        val usedChildren = children.map { it.build(context, depth + 1) }

        return AIActionTree(condition, actions, usedContext, usedChildren)
    }
}

fun actions(
    condition: (Thing, Context) -> Boolean? = { _, _ -> true },
    context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf(),
    initializer: AIActionBuilder.() -> Unit
): List<AIActionTree> {
    return listOf(AIActionBuilder(condition, context).apply(initializer).build())
}
