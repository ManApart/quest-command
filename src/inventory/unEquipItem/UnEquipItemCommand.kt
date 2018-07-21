package inventory.unEquipItem

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Item
import inventory.equipItem.EquipItemEvent
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

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, delimiters)

        if (args.isEmpty()) {
            println("What do you want to un-equip?")
        } else {
            val item = getItem(args)
            if (item != null) {
                EventManager.postEvent(UnEquipItemEvent(GameState.player.creature, item))
            } else {
                println("Could not find ${args.argStrings[0]}")
            }
        }
    }

    private fun getItem(args: Args): Item? {
        val itemName = args.argGroups[0].joinToString(" ")
        val items = GameState.player.creature.body.getEquippedItems()
        return if (items.exists(itemName)){
            items.get(itemName)
        } else {
            null
        }
    }
}