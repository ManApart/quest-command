package core.ai.action.dsl
import core.target.Target
import core.ai.action.AIAction
import core.events.Event

class AIActionBuilder(val condition: (Target) -> Boolean) {
    var priority: Int? = null
    val depthScale: Int = 2
    private val children: MutableList<AIActionBuilder> = mutableListOf()
    private var protoActions: MutableList<ProtoAction> = mutableListOf()

    fun cond(condition: (Target) -> Boolean = { true }, initializer: AIActionBuilder.() -> Unit) {
        children.add(actions(condition, initializer))
    }

    fun action(name: String, result: ((Target) -> Event)) {
        this.protoActions.add(ProtoAction(name) { listOf(result(it)) })
    }

    fun actions(name: String, results: ((Target) -> List<Event>)) {
        this.protoActions.add(ProtoAction(name, results))
    }

    fun build(): List<AIAction> {
        return build(listOf())
    }

    private fun build(parentConditions: List<(Target) -> Boolean>, depth: Int = 0): List<AIAction> {
        val conditions = parentConditions + listOf(condition)
        val evaluations = mutableListOf<AIAction>()
        val usedPriority = priority ?: (10 + depthScale * depth)

        protoActions.forEach { protoAction ->
            evaluations.add(AIAction(protoAction.name, conditions, protoAction.createEvents, usedPriority))
        }

        if (children.isNotEmpty()) {
            evaluations.addAll(children.flatMap { it.build(conditions, depth + 1) })
        }

        return evaluations
    }
}

fun actions(condition: (Target) -> Boolean = { true }, initializer: AIActionBuilder.() -> Unit): AIActionBuilder {
    return AIActionBuilder(condition).apply(initializer)
}
