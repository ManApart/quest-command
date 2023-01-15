package inventory

import core.body.Body
import core.events.EventListener
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import core.utility.then
import explore.listen.addSoundEffect

class ViewInventory : EventListener<ViewInventoryEvent>() {

    override suspend fun execute(event: ViewInventoryEvent) {
        when {
            !event.target.properties.tags.has("Container") -> {
                event.source.displayToMe("Cannot view inventory of ${event.target.name}")
            }
            !event.source.thing.perceives(event.target) -> event.source.displayToMe("You know it's there; you just can't see it.")
            else -> {
                if (event.target.inventory.getItems().isNotEmpty()) {
                    event.source.displayToOthers("${event.source.name} looks at ${event.target.name}'s inventory.")
                    event.source.displayToMe(
                        "${event.target.name} has:${
                            inventoryToString(
                                event.target.inventory,
                                event.target.body
                            )
                        }"
                    )
                    event.source.thing.addSoundEffect("Inventory", "the jostling of leather and metal", 2)
                } else {
                    event.source.displayToMe("${event.target.name} has no items.")
                }
            }
        }

    }

    private suspend fun inventoryToString(inventory: Inventory, body: Body, depth: Int = 0): String {
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

    private suspend fun printItem(item: Thing, body: Body, tabCount: Int): String {
        val asterisk = body.isEquipped(item).then("* ", "")
        val tabs = "\t".repeat(tabCount)
        return "\n" + tabs + asterisk + item.name
    }

    private suspend fun getSortedInventory(inventory: Inventory, body: Body): List<Thing> {
        val equippedItems = inventory.getItems().filter { body.isEquipped(it) }.sortedBy { it.name }
        val unEquippedItems = inventory.getItems().filter { !body.isEquipped(it) }.sortedBy { it.name }
        return equippedItems + unEquippedItems
    }
}