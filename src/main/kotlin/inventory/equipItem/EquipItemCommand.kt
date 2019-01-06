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
    private val delimiters = listOf("to", "on")
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
        val arguments = Args(args, delimiters)

        if (arguments.isEmpty()) {
            //TODO - suggest anything equipable
            display("What do you want to equip?")
        } else {
            val item = getItem(arguments)
            val bodyPartNameGuess = getBodyPart(arguments)
            val body = GameState.player.creature.body
            val force = arguments.contains("f")

            if (item == null) {
                display("Could not find ${arguments.argStrings[0]}. (Did you mean 'equip <item> to <body part>?")
            } else {
                if (!item.canEquipTo(body)) {
                    display("You can't equip ${item.name}.")
                } else {
                    val slot = findSlot(bodyPartNameGuess, body, item)
                    if (slot != null) {
                        if (slot.itemIsEquipped(body) && !force) {
                            confirmEquip(item, bodyPartNameGuess)
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

    private fun getBodyPart(args: Args): String? {
        return if (args.argGroups.size > 1) {
            args.argStrings[1]
        } else {
            null
        }
    }

    //TODO - attachPoint, not body part
    private fun findSlot(bodyPartNameGuess: String?, body: Body, item: Item): Slot? {
        return if (bodyPartNameGuess == null) {
            body.getDefaultSlot(item)
        } else {
            val bodyPart = body.getEquippablePart(bodyPartNameGuess, item)
            if (bodyPart != null) {
                item.findSlot(body, bodyPart.name)
            } else {
                display("Could not find body part $bodyPartNameGuess")
                null
            }
        }
    }

    private fun confirmEquip(newEquip: Item, bodyPartName: String?) {
        //TODO -state equipped item
        display("Replace currently equipped item with ${newEquip.name}?")

        val toPart = if (bodyPartName.isNullOrBlank()) {
            ""
        } else {
            " to $bodyPartName"
        }

        val response = ResponseRequest(mutableMapOf("y" to "equip $newEquip$toPart f", "n" to ""))
        CommandParser.responseRequest = response
    }
}