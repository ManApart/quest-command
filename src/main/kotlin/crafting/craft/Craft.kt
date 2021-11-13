package crafting.craft

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.thing.item.ItemManager
import core.utility.asSubject
import core.utility.isAre
import crafting.DiscoverRecipeEvent
import inventory.Inventory
import inventory.pickupItem.TakeItemEvent

class Craft : EventListener<CraftRecipeEvent>() {

    override fun execute(event: CraftRecipeEvent) {
        val sourceT = event.source.thing
        when {
            event.tool?.isWithinRangeOf(sourceT) == false -> event.source.display{sourceT.asSubject(it) + " " + sourceT.isAre(it) + " too far away to use ${event.tool.name}."}
            event.recipe.canBeCraftedBy(sourceT, event.tool) -> {
                val ingredients = event.recipe.getUsedIngredients(sourceT.inventory.getAllItems())
                val results = event.recipe.getResults(ingredients)
                removeIngredients(sourceT.inventory, ingredients)
                addResults(results, sourceT)
                EventManager.postEvent(DiscoverRecipeEvent(event.source, event.recipe))
//            TODO - Add XP
                event.source.displayToMe("You ${event.recipe.craftVerb} ${ingredients.joinToString(", ") { it.name }} and get ${results.joinToString(", ") { ItemManager.getTaggedItemName(it) }}.")
            }
            else -> event.source.displayToMe("You aren't able to craft ${event.recipe.name}.")
        }
    }

    private fun removeIngredients(inventory: Inventory, ingredients: List<Thing>) {
        ingredients.forEach {
            inventory.remove(it)
        }
    }

    private fun addResults(results: List<Thing>, source: Thing) {
        results.forEach {
            it.location = source.location
            it.position = source.position
            EventManager.postEvent(TakeItemEvent(source, it, silent = true))
        }
    }

}