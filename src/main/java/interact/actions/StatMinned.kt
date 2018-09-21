package interact.actions

import core.events.EventListener
import core.gameState.isPlayer
import status.statChanged.StatMinnedEvent

class StatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return !isPlayer(event.target)
    }

    override fun execute(event: StatMinnedEvent) {
        event.target.consume(event)
    }
}