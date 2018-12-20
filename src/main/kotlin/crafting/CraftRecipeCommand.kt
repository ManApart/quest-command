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
                if (tool == null){
                    //TODO
//                    display("Couldn't find the necessary tools to create ${recipe.result}")
                } else {
//                    val ingredients = getIngredients(recipe.ingredients)
//                    EventManager.postEvent(CookAttemptEvent(GameState.player.creature, ingredients, tool))
                }
            }
            else -> display("Couldn't find recipe ${args.joinToString(" ")}.")
        }
    }

    private fun getIngredients(names: List<String>): List<Item> {
        val ingredients = mutableListOf<Item>()
        names.forEach {
            if (ItemManager.itemExists(it)) {
                ingredients.add(ItemManager.getItem(it))
            }
        }
        return ingredients
    }

}