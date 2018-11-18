package crafting

import core.commands.Args
import core.commands.Command
import core.gameState.Activator
import core.gameState.GameState
import core.gameState.Item
import core.history.display
import interact.ScopeManager
import system.EventManager
import system.ItemManager

class CookCommand : Command() {
    private val delimiters = listOf(",", "with", "on")

    override fun getAliases(): Array<String> {
        return arrayOf("Cook", "Bake")
    }

    override fun getDescription(): String {
        return "Cook:\n\tCook food"
    }

    override fun getManual(): String {
        return "\n\tCook <ingredient>, <ingredient2> on <range>"
    }

    override fun getCategory(): List<String> {
        return listOf("Crafting")
    }

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, delimiters)
        if (!isValidInput(args)){
            display("Make sure to separate ingredients with commas, and then specify what tool you're using by saying on <tool>")
        } else {
            val ingredients = getIngredients(args)
            val tool = getTool(args)

            when {
                tool == null -> display("Couldn't find something to cook on")
                ingredients.size != args.argStrings.size-1 -> display("Couldn't understand all of the ingredients. Found: ${ingredients.joinToString { it.name + ", " }}")
                else -> EventManager.postEvent(CookAttemptEvent(GameState.player.creature, ingredients, tool))

            }
        }
    }

    private fun isValidInput(args: Args): Boolean {
        return args.argGroups.size > 1 && (args.args.contains("on") || args.args.contains("with"))
    }

    private fun getIngredients(args: Args): List<Item> {
        val ingredients = mutableListOf<Item>()
        args.argStrings.subList(0, args.argStrings.size - 1).forEach {
            if (ItemManager.itemExists(it)) {
                ingredients.add(ItemManager.getItem(it))
            }
        }
        return ingredients
    }

    private fun getTool(args: Args): Activator? {
        val group = args.argGroups.last()
        return if (ScopeManager.activatorExists(group)) {
            ScopeManager.getActivator(group)
        } else {
            ScopeManager.findActivatorByTag("Range")
        }
    }

}