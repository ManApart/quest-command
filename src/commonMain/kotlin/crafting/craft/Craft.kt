package crafting.craft

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.utility.asSubject
import core.utility.isAre
import core.utility.joinToStringAnd
import crafting.DiscoverRecipeEvent
import explore.listen.addSoundEffect
import inventory.Inventory
import inventory.pickupItem.TakeItemEvent

class Craft : EventListener<CraftRecipeEvent>() {

    override suspend fun execute(event: CraftRecipeEvent) {
        val sourceT = event.source.thing
        when {
            event.tool?.isWithinRangeOf(sourceT) == false -> event.source.display {
                sourceT.asSubject(it) + " " + sourceT.isAre(
                    it
                ) + " too far away to use ${event.tool.name}."
            }
            event.recipe.canBeCraftedBy(sourceT, event.tool) -> {
                val ingredients = event.recipe.getUsedIngredients(sourceT, sourceT.inventory.getAllItems(), event.tool)
                val usedIngredientList = ingredients.map { it.value.second }
                val results = event.recipe.getResults(event.source.thing, event.tool, ingredients)
                removeIngredients(sourceT.inventory, usedIngredientList)
                addResults(results, sourceT)
                EventManager.postEvent(DiscoverRecipeEvent(event.source, event.recipe))
//            TODO - Add XP
                val ingredientNames = usedIngredientList.joinToStringAnd { it.name }
                val resultNames = results.joinToString(", ") { it.name }
                //TODO - make sound description materially based
                event.source.thing.addSoundEffect("Crafting", "jingles of metal, the scraping of leather, and the pinging of hammers", 20)
                event.source.displayToMe("You ${event.recipe.craftVerb} $ingredientNames together and produce $resultNames.")
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