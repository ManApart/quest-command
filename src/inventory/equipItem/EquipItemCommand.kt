package inventory.equipItem

import core.commands.Args
import core.commands.Command

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

        }
    }
}