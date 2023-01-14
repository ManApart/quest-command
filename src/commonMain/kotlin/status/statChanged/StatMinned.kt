package status.statChanged

import core.events.EventListener

class StatMinned : EventListener<StatMinnedEvent>() {
    override suspend fun shouldExecute(event: StatMinnedEvent): Boolean {
        return !event.thing.isPlayer()
    }

    override suspend fun execute(event: StatMinnedEvent) {
        event.thing.consume(event)
    }
}