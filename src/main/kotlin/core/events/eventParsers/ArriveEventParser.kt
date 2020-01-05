package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import traveling.location.location.LocationPoint
import traveling.arrive.ArriveEvent

class ArriveEventParser : EventParser {
    override fun className(): String {
        return ArriveEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val networkP = 0
        val locationP = 1
        val location = LocationPoint(event.getLocation(parent, networkP, locationP)!!)
        return ArriveEvent(destination = location, method = "move")
    }
}