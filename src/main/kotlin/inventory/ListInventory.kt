package inventory

import core.events.EventListener
import core.gameState.targetsToString
import core.history.display

class ListInventory : EventListener<ListInventoryEvent>() {

    override fun execute(event: ListInventoryEvent) {
        if (event.target.properties.tags.has("Container")) {
            if (event.target.inventory.getAllItems().isNotEmpty()) {
                val itemList = targetsToString(event.target.inventory.getAllItems())
                display("${event.target.name} has $itemList.")
            }else {
                display("${event.target.name} has no items.")
            }
        } else {
            display("Cannot view inventory of ${event.target.name}")
        }
    }
}