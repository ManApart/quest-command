package inventory.equipItem

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Item
import system.EventManager

class EquipItemCommand : Command() {
    private val delimiters = listOf("to", "on")
    override fun getAliases(): Array<String> {
        return arrayOf("Equip")
    }

    override fun getDescription(): String {
        return "Equip:\n\tEquip an item from your inventory"
    }

    override fun getManual(): String {
        return "\n\tEquip <item> - Equip an item" +
                "\n\tEquip <item> to <location> - Equip an item to a specific body part (ex: left hand)"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, delimiters)

        if (args.isEmpty()) {
            println("What do you want to equip?")
        } else {
            val item = getItem(args)
            val bodyPartName = getBodyPart(args)

            if (item != null){
                if (bodyPartName == null){
                    EventManager.postEvent(EquipItemEvent(GameState.player, item))
                } else {
                    if (GameState.player.body.hasPart(bodyPartName)){
                        EventManager.postEvent(EquipItemEvent(GameState.player, item, bodyPartName))
                    } else {
                        println("Could not find body part $bodyPartName")
                    }
                }
            }
        }
    }

    private fun getItem(args: Args): Item? {
        val itemName = args.argGroups[0]
        return if (GameState.player.inventory.itemExists(itemName)) {
            GameState.player.inventory.getItem(itemName)
        } else {
            println("Could not find $itemName")
            null
        }
    }

    private fun getBodyPart(args: Args): String? {
        return if (args.argGroups.size > 1) {
            args.argGroups[1].joinToString(" ")
        } else {
            null
        }
    }
}