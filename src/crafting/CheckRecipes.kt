package crafting

import core.events.EventListener
import core.gameState.GameState

class CheckRecipes : EventListener<CheckRecipeEvent>() {
    override fun shouldExecute(event: CheckRecipeEvent): Boolean {
        return event.source == GameState.player
    }

    override fun execute(event: CheckRecipeEvent) {
        when {
            GameState.player.knownRecipes.isEmpty() -> println("You don't know any recipes yet.")
            event.recipe == null -> printRecipes()
            else -> println(event.recipe.read())
        }
    }

    private fun printRecipes() {
        val recipes = GameState.player.knownRecipes.joinToString { "\n\t${it.name}" }
        println("Recipes:$recipes")
    }


}