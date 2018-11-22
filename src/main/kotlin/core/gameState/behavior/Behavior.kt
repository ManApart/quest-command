package core.gameState.behavior

import core.events.Event
import core.gameState.Target

class Behavior(base: BehaviorBase, paramValues: Map<String, String>) {
    val name = base.name
    private val condition = base.condition.applyParamValues(paramValues)
    private val events = base.events.asSequence().map { it.applyParamValues(paramValues) }.toList()

    fun evaluate(event: Event): Boolean {
        return condition.matches(event)
    }

    fun execute(target: Target) {
        this.events.forEach {
            it.execute(target)
        }
    }
}