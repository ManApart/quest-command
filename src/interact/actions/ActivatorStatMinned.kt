package interact.actions

import core.events.EventListener
import core.gameState.Activator
import core.gameState.consume
import status.StatMinnedEvent

class ActivatorStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.creature.parent != null && event.creature.parent is Activator
    }

    override fun execute(event: StatMinnedEvent) {
        event.creature.parent?.consume(event)
    }
}