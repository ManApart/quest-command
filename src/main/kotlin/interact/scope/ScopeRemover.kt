package interact.scope

import core.events.EventListener
import core.gameState.Item

class ScopeRemover : EventListener<RemoveScopeEvent>() {
    override fun execute(event: RemoveScopeEvent) {
        val scope = ScopeManager.getScope(event.targetLocation)
        if (scope.targetExists(event.target)) {
            scope.removeTarget(event.target)
        } else if (event.target is Item) {
            scope.getAllInventories().forEach {
                if (it.exists(event.target)) {
                    it.remove(event.target)
                    return
                }
            }
        }
    }
}