package interact.scope

import core.events.EventListener
import core.gameState.Item

class ScopeRemover : EventListener<RemoveScopeEvent>() {
    override fun execute(event: RemoveScopeEvent) {
        if (ScopeManager.targetExists(event.target)) {
            ScopeManager.removeTarget(event.target)
        } else if (event.target is Item) {
            ScopeManager.getAllInventories().forEach {
                if (it.exists(event.target)) {
                    it.remove(event.target)
                    return
                }
            }
        }
    }
}