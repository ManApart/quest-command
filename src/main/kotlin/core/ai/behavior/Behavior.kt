package core.ai.behavior

import quests.triggerCondition.TriggerCondition
import quests.triggerCondition.TriggeredEvent
import core.events.Event
import core.events.getValues
import core.target.Target

class Behavior(base: BehaviorBase, paramValues: Map<String, String>) {
    val name = base.name
    private val condition = TriggerCondition(base.condition, paramValues)
    private val triggeredEvents = base.events.asSequence().map { TriggeredEvent(it, paramValues) }.toList()

    fun evaluate(event: Event): Boolean {
        return condition.matches(event)
    }

    fun execute(event: Event, target: Target) {
        val params = event.getValues()
        this.triggeredEvents.forEach {
            TriggeredEvent(it, params).execute(target)
        }
    }
}