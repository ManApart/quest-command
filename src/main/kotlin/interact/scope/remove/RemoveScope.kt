package interact.scope.remove

import core.events.EventListener
import interact.scope.ScopeManager

class RemoveScope : EventListener<RemoveScopeEvent>() {
    override fun execute(event: RemoveScopeEvent) {
        val scope = ScopeManager.getScope(event.targetLocation)
        scope.removeTarget(event.target)
    }
}