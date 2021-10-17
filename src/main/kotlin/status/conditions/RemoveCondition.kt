package status.conditions

import core.events.EventListener
import core.history.display

class RemoveCondition : EventListener<RemoveConditionEvent>() {
    override fun shouldExecute(event: RemoveConditionEvent): Boolean {
        return true
    }

    override fun execute(event: RemoveConditionEvent) {
        if (event.thing.soul.hasCondition(event.condition)) {
            event.thing.soul.removeCondition(event.condition)
            event.thing.display("${event.thing.name} is no longer ${event.condition.name}.")
        }
    }
}