package crafting

import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Inventory
import core.gameState.Item
import core.history.display
import inventory.pickupItem.PickupItemEvent
import system.EventManager

class Craft : EventListener<CraftRecipeEvent>() {

    override fun execute(event: CraftRecipeEvent) {
        if (event.recipe.canBeCraftedBy(event.source, event.tool)) {
            val ingredients = event.recipe.getUsedIngredients(event.source.inventory.getAllItems())
            val results = event.recipe.getResults(ingredients)

            removeIngredients(event.source.inventory, ingredients)
            addResults(results, event)
            EventManager.postEvent(DiscoverRecipeEvent(event.source, event.recipe))
//            TODO - Add XP
            display("You ${event.recipe.craftVerb} ${ingredients.joinToString(", ") { it.name }} and get ${results.joinToString(", ") {it.getTaggedItemName()}}.")
        } else {
            display("You aren't able to craft ${event.recipe.name}.")
        }
    }

    private fun removeIngredients(inventory: Inventory, ingredients: List<Item>) {
        ingredients.forEach {
            inventory.remove(it)
        }
    }

    private fun addResults(results: List<Item>, event: CraftRecipeEvent) {
        results.forEach {
            EventManager.postEvent(PickupItemEvent(event.source, it, null,true))
        }
    }

}