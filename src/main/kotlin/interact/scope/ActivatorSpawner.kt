package interact.scope

import core.events.EventListener
import core.history.display

class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
    override fun execute(event: SpawnActivatorEvent) {
        if (!event.silent) {
            display("${event.activator.name} appeared.")
        }
        ScopeManager.addTarget(event.activator)
    }
}