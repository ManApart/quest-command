package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import traveling.scope.remove.RemoveScopeEvent

class RemoveScopeEventParser : EventParser {
    override fun className(): String {
        return RemoveScopeEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val targetNameP = 0

        val target = event.getTargetOrParent(parent, targetNameP)

        return RemoveScopeEvent(target)
    }
}