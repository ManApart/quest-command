package use.actions

import core.GameState
import core.events.EventManager
import core.history.displayToMe
import crafting.RecipeManager
import crafting.craft.CraftRecipeEvent
import use.UseEvent
import use.UseListener

class UseItemOnIngredientRecipe : UseListener() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (event.used.properties.isItem() && event.thing.properties.isItem()) {
            val recipes = RecipeManager.findCraftableRecipes(listOf(event.thing), event.used, event.source.soul) +
                    RecipeManager.findCraftableRecipes(listOf(event.used), event.thing, event.source.soul) +
                    RecipeManager.findCraftableRecipes(listOf(event.used, event.thing), null, event.source.soul)
            recipes.isNotEmpty()
        } else {
            false
        }
    }

    override fun executeUseEvent(event: UseEvent) {
        val thingRecipes = RecipeManager.findCraftableRecipes(listOf(event.thing), event.used, event.source.soul)
        val sourceRecipes = RecipeManager.findCraftableRecipes(listOf(event.used), event.thing, event.source.soul)
        val itemOnlyRecipes = RecipeManager.findCraftableRecipes(listOf(event.used, event.thing), null, event.source.soul)
        val player = GameState.getPlayer(event.source)

        when {
            thingRecipes.size + sourceRecipes.size + itemOnlyRecipes.size > 1 -> event.source.displayToMe("What do you want to craft? ${(thingRecipes + sourceRecipes + itemOnlyRecipes).joinToString(" or ") { it.name }}")
            thingRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, thingRecipes.first(), event.used))
            sourceRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, sourceRecipes.first(), event.thing))
            itemOnlyRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, itemOnlyRecipes.first(), null))
        }
    }
}