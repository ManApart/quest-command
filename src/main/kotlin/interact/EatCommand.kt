package interact

import core.commands.Command
import core.gameState.GameState
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

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
            val food = ScopeManager.getScope().getItemsIncludingPlayerInventory(argsString).firstOrNull()
            if (food != null) {
                if (food.properties.tags.has("food")){
                    EventManager.postEvent(UseEvent(GameState.player.creature, food, GameState.player.creature))
                } else {
                    display("${food.name} is inedible.")
                }
            } else {
                display("Couldn't find $argsString")
            }
        }
    }

}