package inventory.unEquipItem

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.body.getEquippedItems().map { it.name } + source.body.getParts().map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)

        if (arguments.isEmpty()) {
            clarifyItem(source)
        } else {
            val item = getItem(source.thing, arguments)
            if (item != null) {
                EventManager.postEvent(UnEquipItemEvent(source.thing, item))
            } else {
                val unEquippedItem = getUnequippedItem(source.thing, arguments)
                if (unEquippedItem != null) {
                    source.displayToMe("${unEquippedItem.name} is already unequipped.")
                } else {
                    source.displayToMe("Could not find ${arguments.getBaseString()}")
                }
            }
        }
    }

    private fun getItem(source: Thing, args: Args): Thing? {
        val itemName = args.getBaseString()
        val items = source.body.getEquippedItems()
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

    private fun getUnequippedItem(source: Thing, args: Args): Thing? {
        val itemName = args.getBaseString()
        val equippedItems = source.body.getEquippedItems()
        val items = NameSearchableList(source.inventory.getItems().filter { !equippedItems.contains(it) })
        return if (items.exists(itemName)) {
            items.get(itemName)
        } else {
            null
        }
    }

    private suspend fun clarifyItem(source: Player) {
        source.respond("There are no items you can unequip.") {
            message("What do you want to un-equip?")
            optionsNamed(source.body.getEquippedItems())
            command { "unequip $it" }
        }
    }

}