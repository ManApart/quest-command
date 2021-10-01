package traveling.scope.spawn

import core.GameState
import core.events.EventListener
import core.history.display
import traveling.position.NO_VECTOR

class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
    override fun execute(event: SpawnActivatorEvent) {
        if (!event.silent) {
            display("${event.activator.name} appeared.")
        }
        if (event.position != NO_VECTOR) {
            event.activator.position = event.position
        }
        (event.targetLocation).getLocation().addTarget(event.activator)
    }
}