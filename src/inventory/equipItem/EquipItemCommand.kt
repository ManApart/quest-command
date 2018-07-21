package inventory.equipItem

import core.commands.Command
import core.commands.findDelimiter

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

    override fun execute(keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            println("What do you want to equip?")
        } else {
            val delimiter = findDelimiter(args, delimiters)
        }
    }
}