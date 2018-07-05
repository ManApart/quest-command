package processing

import events.EventListener
import events.PickupItemEvent
import events.DropItemEvent
import gameState.ScopeManager

object InventoryManager {
    class DropHandler : EventListener<DropItemEvent>() {
        override fun execute(event: DropItemEvent) {
            println("${event.source} dropped ${event.item}")
            event.source.inventory.items.remove(event.item)
            ScopeManager.addTarget(event.item)
        }
    }

    class PickupHandler : EventListener<PickupItemEvent>() {
        override fun execute(event: PickupItemEvent) {
            println("${event.source} picked up ${event.item}")
            event.source.inventory.items.add(event.item)
            ScopeManager.removeTarget(event.item)
        }
    }
}