package core.properties

import core.events.EventListener

class SetProperties: EventListener<SetPropertiesEvent>() {

    override fun execute(event: SetPropertiesEvent) {
        event.thing.properties.setFrom(event.properties)
    }
}