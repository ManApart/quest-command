package inventory

import core.body.Body
import core.events.EventListener
import core.history.display
import core.thing.Thing
import core.utility.then

class ListInventory : EventListener<ListInventoryEvent>() {

    override fun execute(event: ListInventoryEvent) {
        if (event.thing.properties.tags.has("Container")) {
            if (event.thing.inventory.getItems().isNotEmpty()) {
                event.thing.display("${event.thing.name} has:${inventoryToString(event.thing.inventory, event.thing.body)}")
            } else {
                event.thing.display("${event.thing.name} has no items.")
            }
        } else {
            event.thing.display("Cannot view inventory of ${event.thing.name}")
        }
    }

    private fun inventoryToString(inventory: Inventory, body: Body, depth: Int = 0): String {
        var message = ""
        getSortedInventory(inventory, body)
                .forEach {
                    message += printItem(it, body, depth + 1)
                    if (it.inventory.getItems().isNotEmpty()) {
                        message += inventoryToString(it.inventory, body, depth + 1)
                    }
                }
        return message
    }

    private fun printItem(item: Thing, body: Body, tabCount: Int): String {
        val asterisk = body.isEquipped(item).then("* ", "")
        val tabs = "\t".repeat(tabCount)
        return "\n" + tabs + asterisk + item.name
    }

    private fun getSortedInventory(inventory: Inventory, body: Body): List<Thing> {
        val equippedItems = inventory.getItems().filter { body.isEquipped(it) }.sortedBy { it.name }
        val unEquippedItems = inventory.getItems().filter { !body.isEquipped(it) }.sortedBy { it.name }
        return equippedItems + unEquippedItems
    }
}