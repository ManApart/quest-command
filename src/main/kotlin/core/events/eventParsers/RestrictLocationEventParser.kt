package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import traveling.RestrictLocationEvent
import traveling.location.LocationManager

class RestrictLocationEventParser : EventParser {
    override fun className(): String {
        return RestrictLocationEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val sourceNetworkP = 0
        val sourceLocationP = 1
        val destinationNetworkP = 2
        val destinationLocationP = 3
        val restrictedP = 4

        val sourceLocation = LocationManager.getNetwork(event.getParam(sourceNetworkP)).getLocationNode(event.getParam(sourceLocationP))
        val destinationLocation = LocationManager.getNetwork(event.getParam(destinationNetworkP)).getLocationNode(event.getParam(destinationLocationP))

        return RestrictLocationEvent(sourceLocation, destinationLocation, event.getParamBoolean(restrictedP))
    }
}