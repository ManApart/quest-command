package inventory.dropItem

import core.events.EventListener
import core.history.display
import interact.scope.ScopeManager

class PlaceItem : EventListener<PlaceItemEvent>() {
    override fun execute(event: PlaceItemEvent) {
        if (!event.silent){
            display("${event.source} dropped ${event.item}")
        }
        event.source.inventory.remove(event.item)
        ScopeManager.getScope(event.source.location).addTarget(event.item)
    }
}