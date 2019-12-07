package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import status.effects.AddConditionEvent

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