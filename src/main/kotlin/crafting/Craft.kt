package crafting

import core.events.EventListener
import core.gameState.Inventory
import core.gameState.Target
import core.history.display
import inventory.dropItem.TransferItemEvent
import system.EventManager
import system.item.ItemManager

class Craft : EventListener<CraftRecipeEvent>() {

    override fun execute(event: CraftRecipeEvent) {
        if (event.recipe.canBeCraftedBy(event.source, event.tool)) {
            val ingredients = event.recipe.getUsedIngredients(event.source.inventory.getAllItems())
            val results = event.recipe.getResults(ingredients)

            removeIngredients(event.source.inventory, ingredients)
            addResults(results, event)
            EventManager.postEvent(DiscoverRecipeEvent(event.source, event.recipe))
//            TODO - Add XP
            display("You ${event.recipe.craftVerb} ${ingredients.joinToString(", ") { it.name }} and get ${results.joinToString(", ") { ItemManager.getTaggedItemName(it) }}.")
        } else {
            display("You aren't able to craft ${event.recipe.name}.")
        }
    }

    private fun removeIngredients(inventory: Inventory, ingredients: List<Target>) {
        ingredients.forEach {
            inventory.remove(it)
        }
    }

    private fun addResults(results: List<Target>, event: CraftRecipeEvent) {
        results.forEach {
            EventManager.postEvent(TransferItemEvent(it, destination = event.source, silent = true))
        }
    }

}