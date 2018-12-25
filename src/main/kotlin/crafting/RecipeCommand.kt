package crafting

import core.commands.Command
import core.gameState.GameState
import core.history.display
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
        return listOf("Crafting")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            args.isEmpty() -> EventManager.postEvent(CheckRecipeEvent(GameState.player))
            GameState.player.knownRecipes.exists(argString) -> EventManager.postEvent(CheckRecipeEvent(GameState.player, GameState.player.knownRecipes.get(argString)))
            else -> display("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

}