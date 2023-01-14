package use.actions

import core.GameState
import core.events.EventManager
import core.history.displayToMe
import crafting.RecipeManager
import crafting.craft.CraftRecipeEvent
import use.UseEvent
import use.UseListener

class UseIngredientOnActivatorRecipe : UseListener() {

    override suspend fun shouldExecute(event: UseEvent): Boolean {
        return if (event.used.properties.isItem() && event.usedOn.properties.isActivator()) {
            RecipeManager.findCraftableRecipes(event.source, listOf(event.used), event.usedOn).isNotEmpty()
        } else {
            false
        }
    }

    override suspend fun executeUseEvent(event: UseEvent) {
        val recipes = RecipeManager.findCraftableRecipes(event.source, listOf(event.used), event.usedOn)

        when {
            recipes.size > 1 -> event.source.displayToMe("What do you want to craft? ${recipes.joinToString(" or ") { it.name }}")
            else -> GameState.getPlayer(event.source)?.let { EventManager.postEvent(CraftRecipeEvent(it, recipes.first(), event.usedOn)) }
        }
    }
}