package status.conditions

import core.events.EventListener
import core.history.display

class RemoveCondition : EventListener<RemoveConditionEvent>() {
    override suspend fun shouldExecute(event: RemoveConditionEvent): Boolean {
        return true
    }

    override suspend fun execute(event: RemoveConditionEvent) {
        if (event.thing.soul.hasCondition(event.condition)) {
            event.thing.soul.removeCondition(event.condition)
            if (!event.condition.silent) {
                event.thing.display("${event.thing.name} is no longer ${event.condition.name}.")
            }
        }
    }
}