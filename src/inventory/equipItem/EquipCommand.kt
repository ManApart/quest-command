package inventory.equipItem

import core.commands.Command

class EquipCommand : Command() {
    private val delimiters = listOf("to", "on")
    override fun getAliases(): Array<String> {
        return arrayOf("Equip")
    }

    override fun getDescription(): String {
        return "Equip:\n\tEquip an item from your inventory"
    }

    override fun getManual(): String {
        return "\n\tUse <item> - Describe an item or activator" +
                "\n\tUse <item> on <target> - Use an item on a target."
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
    }
}