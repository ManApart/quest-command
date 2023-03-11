package use.actions

import core.GameState
import core.events.EventManager
import core.history.displayToMe
import crafting.RecipeManager
import crafting.craft.CraftRecipeEvent
import use.UseEvent
import use.UseListener

class UseItemOnIngredientRecipe : UseListener() {

    override suspend fun shouldExecute(event: UseEvent): Boolean {
        return if (event.used.properties.isItem() && event.usedOn.properties.isItem()) {
            val recipes = RecipeManager.findCraftableRecipes(event.creature, listOf(event.usedOn), event.used) +
                    RecipeManager.findCraftableRecipes(event.creature, listOf(event.used), event.usedOn) +
                    RecipeManager.findCraftableRecipes(event.creature, listOf(event.used, event.usedOn), null)
            recipes.isNotEmpty()
        } else {
            false
        }
    }

    override suspend fun executeUseEvent(event: UseEvent) {
        val thingRecipes = RecipeManager.findCraftableRecipes(event.creature, listOf(event.usedOn), event.used)
        val sourceRecipes = RecipeManager.findCraftableRecipes(event.creature, listOf(event.used), event.usedOn)
        val itemOnlyRecipes = RecipeManager.findCraftableRecipes(event.creature, listOf(event.used, event.usedOn), null)
        val player = GameState.getPlayer(event.creature)

        when {
            player == null -> println("Player is null")
            thingRecipes.size + sourceRecipes.size + itemOnlyRecipes.size > 1 -> event.creature.displayToMe("What do you want to craft? ${(thingRecipes + sourceRecipes + itemOnlyRecipes).joinToString(" or ") { it.name }}")
            thingRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, thingRecipes.first(), event.used))
            sourceRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, sourceRecipes.first(), event.usedOn))
            itemOnlyRecipes.size == 1 -> EventManager.postEvent(CraftRecipeEvent(player, itemOnlyRecipes.first(), null))
        }
    }
}