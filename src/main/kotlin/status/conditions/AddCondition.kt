package status.conditions

import core.events.EventListener
import core.history.display

class AddCondition : EventListener<AddConditionEvent>() {
    override fun shouldExecute(event: AddConditionEvent): Boolean {
        return true
    }

    override fun execute(event: AddConditionEvent) {
        event.thing.soul.addNewCondition(event.condition)
        if (!event.silent) {
            event.thing.display("${event.thing.name} is now ${event.condition.name}")
        }
    }
}