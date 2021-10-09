package use.actions

import core.events.EventManager
import core.history.displayToMe
import crafting.RecipeManager
import crafting.craft.CraftRecipeEvent
import use.UseEvent
import use.UseListener

class UseItemOnIngredientRecipe : UseListener() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (event.used.properties.isItem() && event.target.properties.isItem()) {
            val recipes = RecipeManager.findCraftableRecipes(listOf(event.target), event.used, event.source.soul) +
                    RecipeManager.findCraftableRecipes(listOf(event.used), event.target, event.source.soul) +
                    RecipeManager.findCraftableRecipes(listOf(event.used, event.target), null, event.source.soul)
            recipes.isNotEmpty()
        } else {
            false
        }
    }

    override fun executeUseEvent(event: UseEvent) {
        val targetRecipes = RecipeManager.findCraftableRecipes(listOf(event.target), event.used, event.source.soul)
        val sourceRecipes = RecipeManager.findCraftableRecipes(listOf(event.used), event.target, event.source.soul)
        val itemOnlyRecipes = RecipeManager.findCraftableRecipes(listOf(event.used, event.target), null, event.source.soul)

        when {
            targetRecipes.size + sourceRecipes.size + itemOnlyRecipes.size > 1 -> event.source.displayToMe("What do you want to craft? ${(targetRecipes + sourceRecipes + itemOnlyRecipes).joinToString(" or ") { it.name }}")
            targetRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(event.source, targetRecipes.first(), event.used))
            sourceRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(event.source, sourceRecipes.first(), event.target))
            itemOnlyRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(event.source, itemOnlyRecipes.first(), null))
        }
    }
}