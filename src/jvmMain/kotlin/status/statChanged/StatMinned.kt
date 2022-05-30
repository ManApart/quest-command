package status.statChanged

import core.events.EventListener

class StatMinned : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return !event.thing.isPlayer()
    }

    override fun execute(event: StatMinnedEvent) {
        event.thing.consume(event)
    }
}