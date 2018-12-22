package interact.actions

import core.events.EventListener
import core.gameState.Activator
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

class UseIngredientOnActivatorRecipe : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (event.source is Item && event.target is Activator){
            val recipes = RecipeManager.findCraftableRecipes(listOf(event.source), event.target, GameState.player.creature.soul)
            recipes.isNotEmpty()
        } else {
            false
        }
    }

    override fun execute(event: UseEvent) {
        val recipes = RecipeManager.findCraftableRecipes(listOf(event.source as Item), event.target, GameState.player.creature.soul)

        when {
            recipes.size > 1 -> display("What do you want to craft? ${recipes.joinToString(" or ") { it.name }}")
            else -> EventManager.postEvent(CraftRecipeEvent(GameState.player.creature, recipes.first(), event.target ))
        }
    }
}