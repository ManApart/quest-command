package status.conditions

import core.events.EventListener
import core.history.display

class AddCondition: EventListener<AddConditionEvent>() {
    override fun shouldExecute(event: AddConditionEvent): Boolean {
        return true
    }

    override fun execute(event: AddConditionEvent) {
        event.target.soul.addNewCondition(event.condition)
        display("${event.target} is now ${event.condition.name}")
    }
}