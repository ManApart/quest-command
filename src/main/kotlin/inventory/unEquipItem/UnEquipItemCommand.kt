package inventory.unEquipItem

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import core.utility.NameSearchableList
import system.EventManager

class UnEquipItemCommand : Command() {
    private val delimiters = listOf("from")
    override fun getAliases(): Array<String> {
        return arrayOf("UnEquip")
    }

    override fun getDescription(): String {
        return "UnEquip:\n\tUnEquip an item you're wearing"
    }

    override fun getManual(): String {
        return "\n\tUnEquip <item> - UnEquip an item" +
                "\n\tUnEquip <body part> - UnEquip any items worn on a specific body part (ex: left hand) X"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)

        if (arguments.isEmpty()) {
            //TODO - make request response
            display("What do you want to un-equip?")
        } else {
            val item = getItem(arguments)
            if (item != null) {
                EventManager.postEvent(UnEquipItemEvent(GameState.player, item))
            } else {
                val unEquippedItem = getUnequippedItem(arguments)
                if (unEquippedItem != null) {
                    display("${unEquippedItem.name} is already unequipped.")
                } else {
                    display("Could not find ${arguments.argStrings[0]}")
                }
            }
        }
    }

    private fun getItem(args: Args): Target? {
        val itemName = args.getGroup(0).joinToString(" ")
        val items = GameState.player.body.getEquippedItems()
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

    private fun getUnequippedItem(args: Args): Target? {
        val itemName = args.getGroup(0).joinToString(" ")
        val equippedItems = GameState.player.body.getEquippedItems()
        val items = NameSearchableList(GameState.player.inventory.getItems().filter { !equippedItems.contains(it) })
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

}