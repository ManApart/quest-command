package use.eat

import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import explore.listen.addSoundEffect
import status.statChanged.StatChangeEvent

class EatFood : EventListener<EatFoodEvent>() {

    override suspend fun complete(event: EatFoodEvent) {
        event.creature.displayToMe("You eat ${event.food.name}.")
        event.creature.displayToOthers("${event.creature.name} eats ${event.food.name}.")
        val healAmount = getHealAmount(event.food)
        EventManager.postEvent(StatChangeEvent(event.creature, event.food.name, "Health", healAmount))
        event.creature.currentLocation().removeThingIncludingPlayerInventory(event.creature, event.food)

        if (event.food.canConsume(event)){
            event.food.consume(event)
        }
        event.creature.addSoundEffect("Eating", "a soft crunching sound, mixed with smacks and spurts", 2)
    }

    private fun getHealAmount(item: Thing): Int {
        val base = item.properties.values.getInt("healAmount")
        return when  {
            item.properties.tags.has("Sliced") -> base + 1
            item.properties.tags.has("Roasted") -> (base * 1.25).toInt()
            item.properties.tags.has("Cooked") -> (base * 1.5).toInt()
            else -> base
        }
    }
}