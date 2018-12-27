package inventory

import core.events.EventListener
import core.gameState.targetsToString
import core.history.display

class ListInventory : EventListener<ListInventoryEvent>() {

    override fun execute(event: ListInventoryEvent) {
        if (event.target.properties.tags.has("Container")) {
            val itemList = targetsToString(event.target.inventory.getAllItems())
            display("${event.target.name} has $itemList.")
        } else {
            display("Cannot view inventory of ${event.target.name}")
        }
    }
}