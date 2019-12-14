package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import status.conditions.RemoveConditionEvent

class RemoveConditionEventParser : EventParser {
    override fun className(): String {
        return RemoveConditionEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val targetNameP = 0

        return RemoveConditionEvent(event.getTargetCreatureOrPlayer(parent, targetNameP), event.createCondition())
    }
}