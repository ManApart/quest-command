package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import status.effects.RemoveConditionEvent

class RemoveConditionEventParser : EventParser {
    override fun className(): String {
        return RemoveConditionEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val targetNameP = 0

        return RemoveConditionEvent(event.getTargetCreatureOrPlayer(parent, targetNameP), event.createCondition())
    }
}