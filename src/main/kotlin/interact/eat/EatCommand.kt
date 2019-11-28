package interact.eat

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import interact.UseEvent
import interact.scope.ScopeManager
import system.EventManager

class EatCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Eat")
    }

    override fun getDescription(): String {
        return "Eat:\n\tEat an item"
    }

    override fun getManual(): String {
        return "\n\tEat <item> - Eat an item"
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        val allFood = ScopeManager.getScope().getItemsIncludingPlayerInventory().filter { it.properties.tags.has("food") }
        val pickedFood = ScopeManager.getScope().getItemsIncludingPlayerInventory(argsString)
        val topChoice = pickedFood.firstOrNull { it.name.toLowerCase() == argsString }

        when {
            args.isEmpty() -> eatWhat(allFood)
            pickedFood.isEmpty() -> display("Couldn't find $argsString")
            topChoice != null -> eatFood(topChoice)
            pickedFood.size > 1 -> eatWhat(pickedFood)
            else -> eatFood(pickedFood.first())
        }
    }

    private fun eatWhat(food: List<Target>) {
        val message = "Eat what?\n\t${food.joinToString(", ")}"
        val response = ResponseRequest(message, food.map { it.name to "eat ${it.name}" }.toMap())
         CommandParser.setResponseRequest(response)
    }

    private fun eatFood(food: Target) {
        if (food.properties.tags.has("food")) {
            EventManager.postEvent(UseEvent(GameState.player, food, GameState.player))
        } else {
            display("${food.name} is inedible.")
        }
    }

}