package interact.scope

import core.events.EventListener
import core.gameState.Item

class ScopeRemover : EventListener<RemoveScopeEvent>() {
    override fun execute(event: RemoveScopeEvent) {
        val scope = ScopeManager.getScope(event.targetLocation)
        scope.removeTarget(event.target)
        if (event.target is Item) {
            scope.getAllInventories().forEach {
                it.remove(event.target)
            }
        }
    }
}