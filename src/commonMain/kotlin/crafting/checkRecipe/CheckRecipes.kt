package crafting.checkRecipe

import core.GameState
import core.Player
import core.events.EventListener
import core.history.displayToMe
import crafting.RecipeManager
import system.debug.DebugType

class CheckRecipes : EventListener<CheckRecipeEvent>() {
    override suspend fun shouldExecute(event: CheckRecipeEvent): Boolean {
        return event.source.thing.isPlayer()
    }

    override suspend fun execute(event: CheckRecipeEvent) {
        when {
            RecipeManager.getKnownRecipes(event.source).isEmpty() && !GameState.getDebugBoolean(DebugType.RECIPE_SHOW_ALL) -> event.source.displayToMe("You don't know any recipes yet.")
            event.recipe == null -> printRecipes(event.source)
            else -> event.source.displayToMe(event.recipe.read())
        }
    }

    private suspend fun printRecipes(source: Player) {
        val recipes = RecipeManager.getKnownRecipes(source)
        val recipeString = recipes.joinToString { "\n\t${it.name}" }

        source.displayToMe("Recipes:$recipeString")
    }


}