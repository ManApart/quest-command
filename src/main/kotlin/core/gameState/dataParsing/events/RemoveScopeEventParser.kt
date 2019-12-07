package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import interact.scope.remove.RemoveScopeEvent

class RemoveScopeEventParser : EventParser {
    override fun className(): String {
        return RemoveScopeEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val targetNameP = 0

        val target = event.getTargetOrParent(parent, targetNameP)

        return RemoveScopeEvent(target)
    }
}