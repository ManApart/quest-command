package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import traveling.scope.remove.RemoveItemEvent

class RemoveItemEventParser : EventParser {
    override fun className(): String {
        return RemoveItemEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val itemNameP = 0
        val targetP = 1

        val target = event.getTargetCreature(parent, targetP) ?: parent
        val item = event.getItemOrParent(parent, itemNameP, target)

        return RemoveItemEvent(target, item)
    }
}