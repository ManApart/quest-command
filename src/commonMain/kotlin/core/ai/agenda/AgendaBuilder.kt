package core.ai.agenda

import core.ai.action.AIAction
import core.ai.action.AIActionTree
import core.ai.action.dsl.AIActionBuilder
import core.ai.action.dsl.ActionRecipe
import core.conditional.Context
import core.conditional.ContextData
import core.events.Event
import core.thing.Thing
import core.utility.putAbsent

class AgendaBuilder(private val name: String, private val context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf()) {
    var priority: Int? = null
    private val children: MutableList<String> = mutableListOf()
    private val actionRecipes: MutableList<ActionRecipe> = mutableListOf()

    fun context(name: String, accessor: (Thing, Context) -> Any?) {
        context[name] = accessor
    }

    fun action(name: String, result: ((Thing, Context) -> Event)) {
        this.actionRecipes.add(ActionRecipe(name) { thing, context -> listOf(result(thing, context)) })
    }

    fun actions(name: String, results: ((Thing, Context) -> List<Event>)) {
        this.actionRecipes.add(ActionRecipe(name, results))
    }

    internal fun build(): Agenda {
        return build(mapOf())
    }

    private fun build(parentContext: ContextData, depth: Int = 0): Agenda {
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

