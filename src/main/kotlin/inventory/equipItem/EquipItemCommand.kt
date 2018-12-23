package inventory.equipItem

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Item
import core.history.display
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
                "\n\tEquip <item> to <body part> - Equip an item to a specific body part (ex: left hand) X"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)

        if (arguments.isEmpty()) {
            display("What do you want to equip?")
        } else {
            val item = getItem(arguments)
            val bodyPartNameGuess = getBodyPart(arguments)
            val body = GameState.player.creature.body

            if (item != null){
                if (!item.canEquipTo(body)) {
                    display("You can't equip ${item.name}.")
                } else if (bodyPartNameGuess == null){
                    EventManager.postEvent(EquipItemEvent(GameState.player.creature, item))
                } else {
                    val bodyPart = body.getEquippablePart(bodyPartNameGuess, item)
                    if (bodyPart != null){
                        val slot = item.findSlot(body, bodyPart.name)
                        if (slot != null){
                            EventManager.postEvent(EquipItemEvent(GameState.player.creature, item, slot))
                        } else {
                            display("Could not equip to body part ${bodyPart.name}")
                        }
                    } else {
                        display("Could not find body part $bodyPartNameGuess")
                    }
                }
            } else {
                display("Could not find ${arguments.argStrings[0]}. (Did you mean 'equip <item> to <body part>?")
            }
        }
    }

    private fun getItem(args: Args): Item? {
        val itemName = args.argGroups[0]
        return if (GameState.player.creature.inventory.exists(itemName)) {
            GameState.player.creature.inventory.getItem(itemName)
        } else {
            null
        }
    }

    private fun getBodyPart(args: Args): String? {
        return if (args.argGroups.size > 1) {
            args.argStrings[1]
        } else {
            null
        }
    }
}