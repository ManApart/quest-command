package traveling

import core.events.EventListener
import core.history.display

class RestrictLocation : EventListener<RestrictLocationEvent>() {

    override suspend fun execute(event: RestrictLocationEvent) {
        event.source.getConnection(event.destination)?.restricted = event.makeRestricted
        event.destination.getConnection(event.source)?.restricted = event.makeRestricted

        if (!event.silent) {
            if (event.makeRestricted) {
                event.triggeringThing.display("${event.destination.locationName} is no longer accessible from ${event.source.locationName}.")
            } else {
                event.triggeringThing.display("${event.destination.locationName} is now available from ${event.source.locationName}.")
            }
        }
    }

}