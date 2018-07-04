package commands

import events.UseItemEvent
import gameState.GameState
import gameState.ScopeManager
import processing.EventManager
import processing.ItemManager

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
        val argsString = args.joinToString(" ")
        if (args.isEmpty()) {
            println("${args.joinToString(" ")} not found!")
        } else {
            if (ItemManager.itemExists(args)) {
                val firstItem = ItemManager.getItem(args)
                val remainingArgs = argsString.substring(argsString.indexOf(firstItem.name.toLowerCase())).split(" ")
                if (ScopeManager.targetExists(remainingArgs)) {
                    val target = ScopeManager.getTarget(remainingArgs)
                    println("posting event $firstItem, $target")
                    EventManager.postEvent(UseItemEvent(firstItem, target))
                } else {
                    val description = if (firstItem.description.isNotBlank()) firstItem.description else "Not much to say."
                    println("${firstItem.name}: $description")
                }
            }
        }
    }
}