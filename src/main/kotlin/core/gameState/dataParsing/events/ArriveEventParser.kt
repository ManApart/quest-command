package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import core.gameState.location.LocationPoint
import travel.ArriveEvent

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