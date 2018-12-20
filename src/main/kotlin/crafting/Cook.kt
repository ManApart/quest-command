package crafting

import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.history.display
import inventory.pickupItem.PickupItemEvent
import system.EventManager
import system.ItemManager

class Cook : EventListener<CookAttemptEvent>() {

    override fun execute(event: CookAttemptEvent) {
        if (!crafterHasIngredients(event)) {
            display("You don't have all of those ingredients.")
        } else {
            val recipe = RecipeManager.findRecipe(event.ingredients, event.tool, event.source.soul)
            if (recipe == null) {
                //Maybe still consume the items?
                display("Nothing happens")
            } else {
                //TODO
//                if (ItemManager.itemExists(recipe.result)){
//                    removeIngredients(event)
//                    EventManager.postEvent(PickupItemEvent(event.source, ItemManager.getItem(recipe.result), true))
//                    discoverRecipe(event.source, recipe)
//                    //TODO - Add XP
//                    display("You cook ${event.ingredients.joinToString(",")} and get a ${recipe.result}.")
//                } else {
//                    display("Seems like ${recipe.result} doesn't exist.")
//                }

            }
        }
    }

    private fun crafterHasIngredients(event: CookAttemptEvent): Boolean {
        event.ingredients.forEach {
            if (!event.source.inventory.exists(it.name)) {
                return false
            }
        }
        return true
    }

    private fun removeIngredients(event: CookAttemptEvent) {
        event.ingredients.forEach {
            event.source.inventory.remove(event.source.inventory.getItem(it.name))
        }
    }

    private fun discoverRecipe(source: Creature, recipe: Recipe) {
        if (source == GameState.player.creature){
            if (!GameState.player.knownRecipes.contains(recipe)){
                GameState.player.knownRecipes.add(recipe)
                display("You've discovered how to make ${recipe.name}!")
            }
        }
    }


}