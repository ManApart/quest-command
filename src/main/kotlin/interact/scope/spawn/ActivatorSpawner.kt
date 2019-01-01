package interact.scope.spawn

import core.events.EventListener
import core.history.display
import interact.scope.ScopeManager

class ActivatorSpawner : EventListener<SpawnActivatorEvent>() {
    override fun execute(event: SpawnActivatorEvent) {
        if (!event.silent) {
            display("${event.activator.name} appeared.")
        }
        ScopeManager.getScope(event.targetLocation).addTarget(event.activator)
    }
}