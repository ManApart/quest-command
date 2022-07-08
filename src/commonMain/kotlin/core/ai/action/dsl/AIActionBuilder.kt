package core.ai.action.dsl

import core.ai.action.AIAction
import core.conditional.Context
import core.conditional.ContextData
import core.events.Event
import core.thing.Thing
import core.utility.putAbsent

class AIActionBuilder(val condition: (Thing, Context) -> Boolean?, private val context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf()) {
    var priority: Int? = null
    val depthScale: Int = 2
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

    internal fun build(): List<AIAction> {
        return build(listOf(), mapOf())
    }

    private fun build(parentConditions: List<(Thing, Context) -> Boolean?>, parentContext: ContextData, depth: Int = 0): List<AIAction> {
        val conditions = parentConditions + listOf(condition)
        val evaluations = mutableListOf<AIAction>()
        val usedPriority = priority ?: (10 + depthScale * depth)
        //This context overrides parent context
        parentContext.entries.forEach { (key, value) -> context.putAbsent(key, value) }

        actionRecipes.forEach { protoAction ->
            evaluations.add(AIAction(protoAction.name, Context(context), conditions, protoAction.createEvents, usedPriority))
        }

        if (children.isNotEmpty()) {
            evaluations.addAll(children.flatMap { it.build(conditions, context, depth + 1) })
        }

        return evaluations
    }
}

fun actions(
    condition: (Thing, Context) -> Boolean? = { _, _ -> true },
    context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf(),
    initializer: AIActionBuilder.() -> Unit
): List<AIAction> {
    return AIActionBuilder(condition, context).apply(initializer).build()
}
