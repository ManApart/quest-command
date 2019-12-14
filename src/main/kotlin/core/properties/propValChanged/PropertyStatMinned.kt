package core.properties.propValChanged

import core.events.EventListener

class PropertyStatMinned : EventListener<PropertyStatMinnedEvent>() {
    override fun shouldExecute(event: PropertyStatMinnedEvent): Boolean {
        return !event.target.isPlayer()
    }

    override fun execute(event: PropertyStatMinnedEvent) {
        event.target.consume(event)
    }
}