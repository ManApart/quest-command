package core.properties

import core.events.EventListener

class SetProperties: EventListener<SetPropertiesEvent>() {

    override suspend fun complete(event: SetPropertiesEvent) {
        event.thing.properties.setFrom(event.properties)
    }
}