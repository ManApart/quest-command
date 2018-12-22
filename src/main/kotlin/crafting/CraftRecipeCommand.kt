package crafting

import core.commands.Command
import core.gameState.GameState
import core.gameState.Item
import core.history.display
import interact.ScopeManager
import system.EventManager
import system.ItemManager

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
        when {
            args.isEmpty() -> EventManager.postEvent(CheckRecipeEvent(GameState.player))
            GameState.player.knownRecipes.exists(args) -> {
                val recipe = GameState.player.knownRecipes.get(args)
                val tool = ScopeManager.findActivatorByProperties(recipe.toolProperties)
                if (!recipe.toolProperties.isEmpty() && tool == null){
                    display("Couldn't find the necessary tools to create ${recipe.name}")
                } else {
                    EventManager.postEvent(CraftRecipeEvent(GameState.player.creature, recipe, tool))
                }
            }
            else -> display("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

}