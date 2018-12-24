package inventory.dropItem

import core.events.EventListener
import core.history.display
import interact.scope.ScopeManager

class DropItem : EventListener<DropItemEvent>() {
    override fun execute(event: DropItemEvent) {
        if (!event.silent){
            display("${event.source} dropped ${event.item}")
        }
        event.source.inventory.remove(event.item)
        ScopeManager.addTarget(event.item)
    }
}