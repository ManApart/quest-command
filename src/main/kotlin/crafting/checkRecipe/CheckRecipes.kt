package crafting.checkRecipe

import core.events.EventListener
import core.GameState
import core.history.display

class CheckRecipes : EventListener<CheckRecipeEvent>() {
    override fun shouldExecute(event: CheckRecipeEvent): Boolean {
        return event.source == GameState.player
    }

    override fun execute(event: CheckRecipeEvent) {
        when {
            GameState.player.knownRecipes.isEmpty() -> display("You don't know any recipes yet.")
            event.recipe == null -> printRecipes()
            else -> display(event.recipe.read())
        }
    }

    private fun printRecipes() {
        val recipes = GameState.player.knownRecipes.joinToString { "\n\t${it.name}" }
        display("Recipes:$recipes")
    }


}