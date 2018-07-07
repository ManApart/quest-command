package interact

import core.commands.Command
import system.EventManager
import system.ItemManager

//TODO - refactor to just interact target on target instead of being item based?
class ItemCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Item", "I", "Action")
    }

    override fun getDescription(): String {
        return "Item:\n\tAction and manage Items"
    }

    override fun getManual(): String {
        return "\n\tItem <item> - Describe an item" +
                "\n\tItem <item> on <target> - Action an item on a target."
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(args: List<String>) {
        val argsString = args.joinToString(" ")
        if (args.isEmpty()) {
            println("${args.joinToString(" ")} not found!")
        } else {
            if (ItemManager.itemExists(args)) {
                val firstItem = ItemManager.getItem(args)
                val remainingArgs = argsString.substring(firstItem.name.length).split(" ")
                if (ScopeManager.targetExists(remainingArgs)) {
                    val target = ScopeManager.getTarget(remainingArgs)
                    EventManager.postEvent(UseItemEvent(firstItem, target))
                } else {
                    val description = if (firstItem.description.isNotBlank()) firstItem.description else "Not much to say."
                    println("${firstItem.name}: $description")
                }
            }
        }
    }
}