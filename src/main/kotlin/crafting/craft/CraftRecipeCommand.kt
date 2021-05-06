package crafting.craft

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.GameState
import core.history.display
import core.events.EventManager
import crafting.Recipe

class CraftRecipeCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Craft", "Make", "Build")
    }

    override fun getDescription(): String {
        return "Craft a recipe you know"
    }

    override fun getManual(): String {
        return """
	Craft <Recipe> - Craft a recipe."""
    }

    override fun getCategory(): List<String> {
        return listOf("Crafting")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        val knownRecipes = GameState.player.knownRecipes
        val pickedRecipes = GameState.player.knownRecipes.getAll(argString)

        when {
            args.isEmpty() && knownRecipes.isEmpty() -> display("You don't know any recipes.")
            args.isEmpty() -> chooseRecipe(knownRecipes)
            pickedRecipes.isEmpty() -> chooseRecipe(knownRecipes)
            pickedRecipes.size == 1 -> processRecipe(GameState.player.knownRecipes.get(argString))
            pickedRecipes.size > 1 -> chooseRecipe(pickedRecipes)
            else -> display("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

    private fun chooseRecipe(recipes: List<Recipe>) {
        val message = "Craft which recipe?${recipes.joinToString { "\n\t${it.name}" }}"
        val response = ResponseRequest(message, recipes.associate { it.name to "craft ${it.name}" })
         CommandParser.setResponseRequest(response)
    }

    private fun processRecipe(recipe: Recipe) {
        val tool = GameState.currentLocation().findActivatorsByProperties(recipe.toolProperties).firstOrNull()
                ?: GameState.player.inventory.findItemsByProperties(recipe.toolProperties).firstOrNull()
        if (!recipe.toolProperties.isEmpty() && tool == null) {
            display("Couldn't find the necessary tools to create ${recipe.name}")
        } else if (!recipe.matches(GameState.player.inventory.getAllItems(), tool)) {
            display("Couldn't find all the needed ingredients to create ${recipe.name}.")
        } else {
            EventManager.postEvent(CraftRecipeEvent(GameState.player, recipe, tool))
        }
    }

}