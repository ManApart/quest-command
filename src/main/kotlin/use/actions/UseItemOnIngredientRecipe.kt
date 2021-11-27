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
        return if (event.used.properties.isItem() && event.usedOn.properties.isItem()) {
            val recipes = RecipeManager.findCraftableRecipes(event.source, listOf(event.usedOn), event.used) +
                    RecipeManager.findCraftableRecipes(event.source, listOf(event.used), event.usedOn) +
                    RecipeManager.findCraftableRecipes(event.source, listOf(event.used, event.usedOn), null)
            recipes.isNotEmpty()
        } else {
            false
        }
    }

    override fun executeUseEvent(event: UseEvent) {
        val thingRecipes = RecipeManager.findCraftableRecipes(event.source, listOf(event.usedOn), event.used)
        val sourceRecipes = RecipeManager.findCraftableRecipes(event.source, listOf(event.used), event.usedOn)
        val itemOnlyRecipes = RecipeManager.findCraftableRecipes(event.source, listOf(event.used, event.usedOn), null)
        val player = GameState.getPlayer(event.source)

        when {
            thingRecipes.size + sourceRecipes.size + itemOnlyRecipes.size > 1 -> event.source.displayToMe("What do you want to craft? ${(thingRecipes + sourceRecipes + itemOnlyRecipes).joinToString(" or ") { it.name }}")
            thingRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, thingRecipes.first(), event.used))
            sourceRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, sourceRecipes.first(), event.usedOn))
            itemOnlyRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, itemOnlyRecipes.first(), null))
        }
    }
}