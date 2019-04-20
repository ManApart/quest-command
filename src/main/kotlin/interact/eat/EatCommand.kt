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
        return "Eat:\n\tEat an Target"
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
            val food = ScopeManager.getScope().getItemsIncludingPlayerInventory(argsString)

            when {
                food.isEmpty() -> display("Couldn't find $argsString")
                food.size > 1 -> eatWhat(food)
                else -> eatFood(food.first())
            }
        }
    }

    private fun eatWhat(food: List<Target>) {
        display("Eat what?\n\t${food.joinToString(", ")}")
        val response = ResponseRequest(food.map { it.name to "eat ${it.name}" }.toMap())
        CommandParser.responseRequest  = response
    }

    private fun eatFood(food: Target) {
        if (food.properties.tags.has("food")){
            EventManager.postEvent(UseEvent(GameState.player, food, GameState.player))
        } else {
            display("${food.name} is inedible.")
        }
    }

}