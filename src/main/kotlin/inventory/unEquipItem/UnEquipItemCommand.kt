package inventory.unEquipItem

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.GameState
import core.target.Target
import core.history.display
import core.utility.NameSearchableList
import core.events.EventManager

class UnEquipItemCommand : Command() {
    private val delimiters = listOf("from")
    override fun getAliases(): List<String> {
        return listOf("UnEquip")
    }

    override fun getDescription(): String {
        return "UnEquip an item you're wearing"
    }

    override fun getManual(): String {
        return """
	UnEquip <item> - UnEquip an item
	UnEquip <body part> - UnEquip any items worn on a specific body part (ex: left hand) X"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)

        if (arguments.isEmpty()) {
            clarifyItem()
        } else {
            val item = getItem(arguments)
            if (item != null) {
                EventManager.postEvent(UnEquipItemEvent(GameState.player, item))
            } else {
                val unEquippedItem = getUnequippedItem(arguments)
                if (unEquippedItem != null) {
                    display("${unEquippedItem.name} is already unequipped.")
                } else {
                    display("Could not find ${arguments.getBaseString()}")
                }
            }
        }
    }

    private fun getItem(args: Args): Target? {
        val itemName = args.getBaseString()
        val items = GameState.player.body.getEquippedItems()
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

    private fun getUnequippedItem(args: Args): Target? {
        val itemName = args.getBaseString()
        val equippedItems = GameState.player.body.getEquippedItems()
        val items = NameSearchableList(GameState.player.inventory.getItems().filter { !equippedItems.contains(it) })
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

    private fun clarifyItem() {
        val targets = GameState.player.body.getEquippedItems()
        val message = "What do you want to un-equip?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.associate { "$it" to "unequip $it" }))
    }

}