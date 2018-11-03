package crafting

import core.commands.Command
import core.gameState.GameState
import system.EventManager

class RecipeCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Recipe", "Recipes")
    }

    override fun getDescription(): String {
        return "Recipe:\n\tView your recipes"
    }

    override fun getManual(): String {
        return "\n\tRecipe - View the Recipes that you know." +
                "\n\tRecipe <Recipe> - View the details of a recipe."
    }

    override fun getCategory(): List<String> {
        return listOf("Craft")
    }

    override fun execute(keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> EventManager.postEvent(CheckRecipeEvent(GameState.player))
            GameState.player.knownRecipes.exists(args) -> EventManager.postEvent(CheckRecipeEvent(GameState.player, GameState.player.knownRecipes.get(args)))
            else -> println("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

}