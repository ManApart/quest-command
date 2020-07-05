package core.events.eventParsers

import core.commands.commandEvent.CommandEvent
import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent

class CommandEventParser : EventParser {
    override fun className(): String {
        return CommandEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val commandP = 0
        val command = event.getParam(commandP)

        return CommandEvent(command)
    }


}