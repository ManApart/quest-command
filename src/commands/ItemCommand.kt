package commands

import gameState.GameState

class ItemCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Item", "I")
    }

    override fun getDescription(): String {
        return "Item:\n\tUse and manage Items"
    }

    override fun getManual(): String {
        return "\n\tItem <item> - Describe an item" +
                "\n\tItem <item> on <target> - Use an item on a target. X"
    }

    override fun execute(args: List<String>) {
        val itemName = args.joinToString(" ")
        if (args.isNotEmpty() && GameState.itemExists(itemName)){
            val item = GameState.getItem(itemName)
            val description = if (item.description.isNotBlank()) item.description else "Not much to say."
            println("${item.name}: $description")
        } else {
            println("${args.joinToString(" ") } not found!")
        }
    }
}