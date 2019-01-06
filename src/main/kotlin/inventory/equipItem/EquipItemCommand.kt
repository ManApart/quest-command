package inventory.equipItem

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.body.Body
import core.gameState.GameState
import core.gameState.Item
import core.gameState.body.Slot
import core.history.display
import system.EventManager

class EquipItemCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Equip")
    }

    override fun getDescription(): String {
        return "Equip:\n\tEquip an item from your inventory"
    }

    override fun getManual(): String {
        return "\n\tEquip <item> - Equip an item" +
                "\n\tEquip <item> to <body part> - Equip an item to a specific body part (ex: left hand). X" +
                "\n\tEquip <item> to <body part> f - Equip an item even if that means unequipping what's already equipped there. X"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, listOf("to", "on"), listOf("f"))

        if (arguments.isEmpty()) {
            suggestEquippableItems()
        } else {
            val item = getItem(arguments)
            val attachPointGuess = getAttachPoint(arguments)
            val body = GameState.player.creature.body
            val force = arguments.has("f")

            if (item == null) {
                display("Could not find ${arguments.argStrings[0]}. (Did you mean 'equip <item> to <body part>?")
            } else {
                if (!item.canEquipTo(body)) {
                    display("You can't equip ${item.name}.")
                } else {
                    val slot = findSlot(attachPointGuess, body, item)
                    if (slot == null) {
                        suggestAttachPoints(attachPointGuess, item)
                    } else {
                        val equippedItems = slot.getEquippedItems(body)
                        if (equippedItems.isNotEmpty() && !force) {
                            confirmEquip(item, equippedItems, attachPointGuess)
                        } else {
                            EventManager.postEvent(EquipItemEvent(GameState.player.creature, item, slot))
                        }
                    }
                }
            }
        }
    }

    private fun getItem(args: Args): Item? {
        val itemName = args.argStrings[0]
        return GameState.player.creature.inventory.getItem(itemName)
    }

    private fun getAttachPoint(args: Args): String? {
        return if (args.argGroups.size > 1) {
            args.argStrings[1]
        } else {
            null
        }
    }

    private fun findSlot(attachPointGuess: String?, body: Body, item: Item): Slot? {
        return if (attachPointGuess == null) {
            body.getDefaultSlot(item)
        } else {
            item.findSlot(body, attachPointGuess)
        }
    }

    private fun suggestEquippableItems() {
        val equippableItems = getEquipableItems()
        display("What do you want to equip?\n\t${equippableItems.joinToString(", ")}")
        val response = ResponseRequest(equippableItems.map { it.name to "equip ${it.name}" }.toMap())
        CommandParser.responseRequest = response
    }

    private fun getEquipableItems(): List<Item> {
        val body = GameState.player.creature.body
        val equippedItems = body.getEquippedItems()
        return GameState.player.creature.inventory.getAllItems().filter { it.canEquipTo(body) && !equippedItems.contains(it) }
    }

    private fun suggestAttachPoints(attachPointGuess: String?, item: Item) {
        display("Could not find attach point $attachPointGuess. Where would you like to equip $item?\n\t${item.equipSlots.joinToString("\n\t")}")
        val response = ResponseRequest(item.equipSlots.flatMap { it.attachPoints }.map { it to "equip $item to $it" }.toMap())
        CommandParser.responseRequest = response
    }

    private fun confirmEquip(newEquip: Item, equippedItems: List<Item>, attachPoint: String?) {
        display("Replace ${equippedItems.joinToString(", ")} with ${newEquip.name}?")

        val toPart = if (attachPoint.isNullOrBlank()) {
            ""
        } else {
            " to $attachPoint"
        }

        val response = ResponseRequest(mapOf("y" to "equip $newEquip$toPart f", "n" to ""))
        CommandParser.responseRequest = response
    }
}