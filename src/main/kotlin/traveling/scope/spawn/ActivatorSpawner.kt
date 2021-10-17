package traveling.scope.spawn

import core.events.EventListener
import core.history.display
import traveling.position.NO_VECTOR

class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
    override fun execute(event: SpawnActivatorEvent) {
        if (!event.silent) {
            event.activator.display("${event.activator.name} appeared.")
        }
        if (event.position != NO_VECTOR) {
            event.activator.position = event.position
        }
        (event.thingLocation).getLocation().addThing(event.activator)
    }
}