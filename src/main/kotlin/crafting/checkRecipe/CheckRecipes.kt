package crafting.checkRecipe

import core.events.EventListener
import core.history.displayYou
import core.target.Target

class CheckRecipes : EventListener<CheckRecipeEvent>() {
    override fun shouldExecute(event: CheckRecipeEvent): Boolean {
        return event.source.isPlayer()
    }

    override fun execute(event: CheckRecipeEvent) {
        when {
            event.source.knownRecipes.isEmpty() -> event.source.displayYou("You don't know any recipes yet.")
            event.recipe == null -> printRecipes(event.source)
            else -> event.source.displayYou(event.recipe.read())
        }
    }

    private fun printRecipes(source: Target) {
        val recipes = source.knownRecipes.joinToString { "\n\t${it.name}" }
        source.displayYou("Recipes:$recipes")
    }


}