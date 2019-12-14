package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import system.message.MessageEvent

class MessageEventParser : EventParser {
    override fun className(): String {
        return MessageEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val messageP = 0

        return MessageEvent(event.getParam(messageP))
    }
}