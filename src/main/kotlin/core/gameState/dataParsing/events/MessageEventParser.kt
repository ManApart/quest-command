package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import system.MessageEvent

class MessageEventParser : EventParser {
    override fun className(): String {
        return MessageEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val messageP = 0

        return MessageEvent(event.getParam(messageP))
    }
}