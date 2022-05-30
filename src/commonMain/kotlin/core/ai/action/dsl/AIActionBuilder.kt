package core.ai.action.dsl
import core.ai.action.AIAction
import core.events.Event
import core.thing.Thing

class AIActionBuilder(val condition: (Thing) -> Boolean) {
    var priority: Int? = null
    val depthScale: Int = 2
    private val children: MutableList<AIActionBuilder> = mutableListOf()
    private var actionRecipes: MutableList<ActionRecipe> = mutableListOf()

    fun cond(condition: (Thing) -> Boolean = { true }, initializer: AIActionBuilder.() -> Unit) {
        children.add(AIActionBuilder(condition).apply(initializer))
    }

    fun action(name: String, result: ((Thing) -> Event)) {
        this.actionRecipes.add(ActionRecipe(name) { listOf(result(it)) })
    }

    fun actions(name: String, results: ((Thing) -> List<Event>)) {
        this.actionRecipes.add(ActionRecipe(name, results))
    }

    internal fun build(): List<AIAction> {
        return build(listOf())
    }

    private fun build(parentConditions: List<(Thing) -> Boolean>, depth: Int = 0): List<AIAction> {
        val conditions = parentConditions + listOf(condition)
        val evaluations = mutableListOf<AIAction>()
        val usedPriority = priority ?: (10 + depthScale * depth)

        actionRecipes.forEach { protoAction ->
            evaluations.add(AIAction(protoAction.name, conditions, protoAction.createEvents, usedPriority))
        }

        if (children.isNotEmpty()) {
            evaluations.addAll(children.flatMap { it.build(conditions, depth + 1) })
        }

        return evaluations
    }
}

fun actions(condition: (Thing) -> Boolean = { true }, initializer: AIActionBuilder.() -> Unit): List<AIAction> {
    return AIActionBuilder(condition).apply(initializer).build()
}
