package interact.actions

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import core.gameState.Stat
import core.gameState.consume
import interact.UseEvent
import status.StatMinnedEvent

class ActivatorStatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.creature is Activator
    }

    override fun execute(event: StatMinnedEvent) {
        event.creature.consume(event)
    }
}