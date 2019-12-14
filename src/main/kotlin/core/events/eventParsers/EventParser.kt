package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent

interface EventParser {
    fun parse(event: TriggeredEvent, parent: Target): Event
    fun className() : String

}