package inventory.unEquipItem

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.NameSearchableList

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

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)

        if (arguments.isEmpty()) {
            clarifyItem(source)
        } else {
            val item = getItem(source, arguments)
            if (item != null) {
                EventManager.postEvent(UnEquipItemEvent(source, item))
            } else {
                val unEquippedItem = getUnequippedItem(source, arguments)
                if (unEquippedItem != null) {
                    display("${unEquippedItem.name} is already unequipped.")
                } else {
                    display("Could not find ${arguments.getBaseString()}")
                }
            }
        }
    }

    private fun getItem(source: Target, args: Args): Target? {
        val itemName = args.getBaseString()
        val items = source.body.getEquippedItems()
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

    private fun getUnequippedItem(source: Target, args: Args): Target? {
        val itemName = args.getBaseString()
        val equippedItems = source.body.getEquippedItems()
        val items = NameSearchableList(source.inventory.getItems().filter { !equippedItems.contains(it) })
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

    private fun clarifyItem(source: Target) {
        val targets = source.body.getEquippedItems()
        val message = "What do you want to un-equip?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.associate { "$it" to "unequip $it" }))
    }

}