package core.gameState.behavior

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggerCondition
import core.gameState.dataParsing.TriggeredEvent

class Behavior(base: BehaviorBase, paramValues: Map<String, String>) {
    val name = base.name
    private val condition = TriggerCondition(base.condition, paramValues)
    private val events = base.events.asSequence().map { TriggeredEvent(it, paramValues) }.toList()

    fun evaluate(event: Event): Boolean {
        return condition.matches(event)
    }

    fun execute(target: Target) {
        this.events.forEach {
            it.execute(target)
        }
    }
}