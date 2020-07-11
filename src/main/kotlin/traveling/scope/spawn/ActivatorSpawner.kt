package traveling.scope.spawn

import core.GameState
import core.events.EventListener
import traveling.position.NO_VECTOR
import core.history.display

class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
    override fun execute(event: SpawnActivatorEvent) {
        if (!event.silent) {
            display("${event.activator.name} appeared.")
        }
        if (event.position != NO_VECTOR) {
            event.activator.position = event.position
        }
        (event.targetLocation ?: GameState.player.location).getLocation().addTarget(event.activator)
    }
}