package interact

import core.commands.Command
import core.gameState.GameState
import core.gameState.Item
import core.gameState.Target
import core.history.display
import interact.scope.ScopeManager
import system.EventManager
import system.ItemManager

class EatCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Eat")
    }

    override fun getDescription(): String {
        return "Eat:\n\tEat an Item"
    }

    override fun getManual(): String {
        return "\n\tEat <item> - Eat an item"
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        if (args.isEmpty()) {
            display("${args.joinToString(" ")} not found!")
        } else {
            if (targetExists(args)) {
                val food = findTarget(args)
                if (food is Item && food.properties.tags.has("food")){
                    EventManager.postEvent(UseEvent(food, GameState.player))
                } else {
                    val description = if (food.description.isNotBlank()) food.description else "Not much to say."
                    display("${food.name}: $description")
                }
            } else {
                display("Couldn't find $argsString")
            }
        }
    }

    private fun targetExists(args: List<String>): Boolean {
        return ScopeManager.targetExists(args) || ItemManager.itemExists(args)
    }

    private fun findTarget(args: List<String>): Target {
        return if (ItemManager.itemExists(args)) {
            ItemManager.getItem(args)
        } else {
            ScopeManager.getTarget(args)
        }
    }

}