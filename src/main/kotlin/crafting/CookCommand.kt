package crafting

import core.commands.Args
import core.commands.Command
import core.gameState.Activator
import core.gameState.GameState
import core.gameState.Item
import core.history.display
import interact.scope.ScopeManager
import system.EventManager
import system.ItemManager

class CookCommand : Command() {
    private val delimiters = listOf(",", "with", "on")

    override fun getAliases(): Array<String> {
        return arrayOf("Cook", "Bake")
    }

    override fun getDescription(): String {
        return "Craft:\n\tCraft food"
    }

    override fun getManual(): String {
        return "\n\tCraft <ingredient>, <ingredient2> on <range>"
    }

    override fun getCategory(): List<String> {
        return listOf("Crafting")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)
        if (!isValidInput(arguments)) {
            display("Make sure to separate ingredients with commas, and then specify what tool you're using by saying on <tool>")
        } else {
            val ingredients = getIngredients(arguments)
            val tool = getTool(arguments)
            val recipes = RecipeManager.findCraftableRecipes(ingredients, tool, GameState.player.creature.soul)

            when {
                tool == null -> display("Couldn't find something to cook on")
                ingredients.size != arguments.argStrings.size - 1 -> display("Couldn't understand all of the ingredients. Found: ${ingredients.joinToString { it.name + ", " }}")
                recipes.isEmpty() -> display("Couldn't find a recipe for those ingredients")
                recipes.size > 1 -> display("What do you want to craft? ${recipes.joinToString(" or ") { it.name }}")
                else -> EventManager.postEvent(CraftRecipeEvent(GameState.player.creature, recipes.first(), tool))
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
        val scope = ScopeManager.getScope()
        return scope.getActivator(group.joinToString(" ")) ?: scope.findActivatorsByTag("Range").firstOrNull()
    }

}