package crafting

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

class CraftRecipeCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Craft", "Make", "Build")
    }

    override fun getDescription(): String {
        return "Craft:\n\tCraft a recipe you know"
    }

    override fun getManual(): String {
        return "\n\tCraft - View the Recipes that you know." +
                "\n\tCraft <Recipe> - Craft a recipe."
    }

    override fun getCategory(): List<String> {
        return listOf("Crafting")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        if (args.isEmpty()) {
            EventManager.postEvent(CheckRecipeEvent(GameState.player))
        } else {
            val recipes = GameState.player.knownRecipes.getAll(argString)
            when {
                recipes.size > 1 -> chooseRecipe(recipes)
                recipes.size == 1 -> processRecipe(GameState.player.knownRecipes.get(argString))
                else -> display("Couldn't find recipe ${args.joinToString(" ")}.")
            }
        }
    }

    private fun chooseRecipe(recipes: List<Recipe>) {
        display("Craft which recipe?\n\t${recipes.joinToString(", ")}")
        val response = ResponseRequest(recipes.map { it.name to "craft ${it.name}" }.toMap())
        CommandParser.setNextResponse(response)
    }

    private fun processRecipe(recipe: Recipe) {
        val tool = ScopeManager.getScope().findActivatorsByProperties(recipe.toolProperties).firstOrNull()
                ?: GameState.player.creature.inventory.findItemsByProperties(recipe.toolProperties).firstOrNull()
        if (!recipe.toolProperties.isEmpty() && tool == null) {
            display("Couldn't find the necessary tools to create ${recipe.name}")
        } else if (!recipe.matches(GameState.player.creature.inventory.getAllItems(), tool)) {
            display("Couldn't find all the needed ingredients to create ${recipe.name}.")
        } else {
            EventManager.postEvent(CraftRecipeEvent(GameState.player.creature, recipe, tool))
        }
    }

}