package inventory

import core.events.EventListener
import core.gameState.Inventory
import core.gameState.Item
import core.gameState.body.Body
import core.gameState.getCreature
import core.history.display
import core.utility.StringFormatter

class ListInventory : EventListener<ListInventoryEvent>() {

    override fun execute(event: ListInventoryEvent) {
        if (event.target.properties.tags.has("Container")) {
            if (event.target.inventory.getItems().isNotEmpty()) {
                display("${event.target.name} has:${inventoryToString(event.target.inventory, event.target.getCreature()?.body)}")
            }else {
                display("${event.target.name} has no items.")
            }
        } else {
            display("Cannot view inventory of ${event.target.name}")
        }
    }

    private fun inventoryToString(inventory: Inventory, body: Body?, depth: Int = 0) : String {
        var message = ""
        inventory.getItems().forEach {
            message += printItem(it, body, depth+1)
            if (it.inventory.getItems().isNotEmpty()){
                message += inventoryToString(it.inventory, body, depth+1)
            }
        }
        return message
    }

    private fun printItem(item: Item, body: Body?, tabCount: Int): String {
        val asterisk = StringFormatter.format(body != null && body.isEquipped(item), "* ", "")
        val tabs = "\t".repeat(tabCount)
        return "\n" + tabs + asterisk + item.name
    }
}