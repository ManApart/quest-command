package interact.actions

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Item
import core.gameState.getSoul
import core.history.display
import crafting.CraftRecipeEvent
import crafting.RecipeManager
import interact.UseEvent
import status.effects.AddEffectEvent
import status.effects.EffectManager
import system.EventManager

class UseItemOnIngredientRecipe : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (event.used is Item && event.target is Item) {
            val recipes = RecipeManager.findCraftableRecipes(listOf(event.target), event.used, event.source.soul) +
                    RecipeManager.findCraftableRecipes(listOf(event.used), event.target, event.source.soul) +
                    RecipeManager.findCraftableRecipes(listOf(event.used, event.target), null, event.source.soul)
            recipes.isNotEmpty()
        } else {
            false
        }
    }

    override fun execute(event: UseEvent) {
        val targetRecipes = RecipeManager.findCraftableRecipes(listOf(event.target as Item), event.used, event.source.soul)
        val sourceRecipes = RecipeManager.findCraftableRecipes(listOf(event.used as Item), event.target, event.source.soul)
        val itemOnlyRecipes = RecipeManager.findCraftableRecipes(listOf(event.used, event.target), null, event.source.soul)

        when {
            targetRecipes.size + sourceRecipes.size + itemOnlyRecipes.size > 1 -> display("What do you want to craft? ${(targetRecipes + sourceRecipes + itemOnlyRecipes).joinToString(" or ") { it.name }}")
            targetRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(event.source, targetRecipes.first(), event.used))
            sourceRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(event.source, sourceRecipes.first(), event.target))
            itemOnlyRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(event.source, itemOnlyRecipes.first(), null))
        }
    }
}