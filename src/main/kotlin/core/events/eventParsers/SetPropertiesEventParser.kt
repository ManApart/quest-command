package core.events.eventParsers

import core.events.Event
import core.properties.SetPropertiesEvent
import core.target.Target
import quests.triggerCondition.TriggeredEvent

class SetPropertiesEventParser : EventParser {
    override fun className(): String {
        return SetPropertiesEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val targetP = 0
        val typeP = 1
        val keyP = 2
        val valueP = 3

        val target = event.getTargetCreatureOrPlayer(parent, targetP)
        val properties = event.getProperties(typeP, keyP, valueP)

        return SetPropertiesEvent(target, properties)
    }
}