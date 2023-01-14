package crafting.checkRecipe

import core.GameState
import core.GameState.player
import core.Player
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import crafting.RecipeManager
import system.debug.DebugType

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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("all") + RecipeManager.getKnownRecipes(source).map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            args.isEmpty() && keyword == "recipe" -> clarifyRecipe(source)
            args.isEmpty() -> EventManager.postEvent(CheckRecipeEvent(source))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(CheckRecipeEvent(source))
            args.size == 1 && args[0] == "recipe" -> clarifyWhichRecipe(source)
            RecipeManager.getKnownRecipes(source).exists(argString) -> EventManager.postEvent(CheckRecipeEvent(source, RecipeManager.getKnownRecipes(source).get(argString)))
            GameState.getDebugBoolean(DebugType.RECIPE_SHOW_ALL) && RecipeManager.recipeExists(argString) -> EventManager.postEvent(CheckRecipeEvent(source, RecipeManager.getRecipe(argString)))
            else -> source.displayToMe("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyRecipe(player: Player) {
        player.respond({}) {
            message("List all or read a specific recipe?")
            options("All", "Recipe")
            command { "recipe $it" }
        }
    }

    private fun clarifyWhichRecipe(player: Player) {
        player.respond("You don't know any recipes.") {
            message("Read what recipe?")
            optionsNamed(RecipeManager.getKnownRecipes(player))
            command { "recipe $it" }
        }
    }

}