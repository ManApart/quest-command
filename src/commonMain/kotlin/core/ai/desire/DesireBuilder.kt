package core.ai.desire

import core.thing.Thing

class DesireBuilder(val condition: (Thing) -> Boolean?, private val context: MutableMap<String, (Thing) -> Any?> = mutableMapOf()) {
    var priority: Int? = null
    private val depthScale: Int = 2
    private val children: MutableList<DesireBuilder> = mutableListOf()
    private val agendas: MutableList<String> = mutableListOf()


    fun cond(condition: (Thing) -> Boolean? = { _ -> true }, initializer: DesireBuilder.() -> Unit) {
        children.add(DesireBuilder(condition, context.toMutableMap()).apply(initializer))
    }

    fun agenda(agenda: String) {
        agendas.add(agenda)
    }

    internal fun build(depth: Int = 0): DesireTree {
        val usedPriority = priority ?: (10 + depthScale * depth)
        val usedAgendas = agendas.map { Pair(it, usedPriority) }
        val usedChildren = children.map { it.build(depth + 1) }

        return DesireTree(condition, usedAgendas, usedChildren)
    }
}

fun desires(
    condition: (Thing) -> Boolean? = { _ -> true },
    initializer: DesireBuilder.() -> Unit
): List<DesireTree> {
    return listOf(DesireBuilder(condition).apply(initializer).build())
}
