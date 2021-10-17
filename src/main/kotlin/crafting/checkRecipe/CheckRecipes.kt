package crafting.checkRecipe

import core.Player
import core.events.EventListener
import core.history.displayToMe

class CheckRecipes : EventListener<CheckRecipeEvent>() {
    override fun shouldExecute(event: CheckRecipeEvent): Boolean {
        return event.source.thing.isPlayer()
    }

    override fun execute(event: CheckRecipeEvent) {
        when {
            event.source.knownRecipes.isEmpty() -> event.source.displayToMe("You don't know any recipes yet.")
            event.recipe == null -> printRecipes(event.source)
            else -> event.source.displayToMe(event.recipe.read())
        }
    }

    private fun printRecipes(source: Player) {
        val recipes = source.knownRecipes.joinToString { "\n\t${it.name}" }
        source.displayToMe("Recipes:$recipes")
    }


}