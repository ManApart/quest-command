package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import status.conditions.AddConditionEvent

class AddConditionEventParser : EventParser {
    override fun className(): String {
        return AddConditionEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val targetNameP = 0

        //TODO - actually parse out conditions etc
        return AddConditionEvent(event.getTargetCreatureOrPlayer(parent, targetNameP), event.createCondition())
    }
}