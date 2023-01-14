package core.properties.propValChanged

import core.events.EventListener

class PropertyStatMinned : EventListener<PropertyStatMinnedEvent>() {
    override suspend fun shouldExecute(event: PropertyStatMinnedEvent): Boolean {
        return !event.thing.isPlayer()
    }

    override suspend fun execute(event: PropertyStatMinnedEvent) {
        event.thing.consume(event)
    }
}