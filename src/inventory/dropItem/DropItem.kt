package inventory.dropItem

import core.events.EventListener
import interact.ScopeManager

class DropItem : EventListener<DropItemEvent>() {
    override fun execute(event: DropItemEvent) {
        println("${event.source} dropped ${event.item}")
        event.source.inventory.items.remove(event.item)
        ScopeManager.addTarget(event.item)
    }
}