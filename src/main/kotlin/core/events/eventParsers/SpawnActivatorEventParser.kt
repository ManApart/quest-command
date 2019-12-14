package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import traveling.scope.spawn.SpawnActivatorEvent
import core.target.activator.ActivatorManager

class SpawnActivatorEventParser : EventParser {
    override fun className(): String {
        return SpawnActivatorEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val activatorNameP = 0
        val countP = 1
        val targetLocationNetworkP = 2
        val targetLocationP = 3
        val positionP = 4
        val positionParentP = 5


        val targetLocation = event.getLocation(parent, targetLocationNetworkP, targetLocationP)
        val positionParent = event.getTargetCreature(parent, positionParentP)
        return SpawnActivatorEvent(ActivatorManager.getActivator(event.getParam(activatorNameP)), event.getParamBoolean(countP), targetLocation, positionParent = positionParent)
    }
}