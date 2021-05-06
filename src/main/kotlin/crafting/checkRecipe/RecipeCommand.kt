package crafting.checkRecipe

import core.GameState
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display

class RecipeCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Recipe", "Recipes")
    }

    override fun getDescription(): String {
        return "View your recipes"
    }

    override fun getManual(): String {
        return """
	Recipe all - View the Recipes that you know.
	Recipe <Recipe> - View the details of a recipe."""
    }

    override fun getCategory(): List<String> {
        return listOf("Crafting")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            args.isEmpty() && keyword == "recipe" -> clarifyRecipe()
            args.isEmpty() -> EventManager.postEvent(CheckRecipeEvent(GameState.player))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(CheckRecipeEvent(GameState.player))
            args.size == 1 && args[0] == "recipe" -> clarifyWhichRecipe()
            GameState.player.knownRecipes.exists(argString) -> EventManager.postEvent(CheckRecipeEvent(GameState.player, GameState.player.knownRecipes.get(argString)))
            else -> display("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyRecipe() {
        val targets = listOf("All", "Recipe")
        val message = "List all or read a specific recipe?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "recipe $it" }))
    }

    private fun clarifyWhichRecipe() {
        val targets = GameState.player.knownRecipes.map { it.name }
        val message = "Read what recipe?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest(ResponseRequest(message, targets.associateWith { "recipe $it" }))
    }

}