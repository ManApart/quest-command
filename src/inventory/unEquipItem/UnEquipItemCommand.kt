package inventory.unEquipItem

import core.commands.Command

class UnEquipItemCommand : Command() {
    private val delimiters = listOf("to", "on")
    override fun getAliases(): Array<String> {
        return arrayOf("UnEquip")
    }

    override fun getDescription(): String {
        return "UnEquip:\n\tUnEquip an item you're wearing"
    }

    override fun getManual(): String {
        return "\n\tUnEquip <item> - UnEquip an item" +
                "\n\tUnEquip <location> - UnEquip any items worn on a specific body part (ex: left hand)"
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
    }
}