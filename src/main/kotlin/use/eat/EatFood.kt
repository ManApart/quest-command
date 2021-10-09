package use.eat

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.then
import status.statChanged.StatChangeEvent

class EatFood : EventListener<EatFoodEvent>() {

    override fun execute(event: EatFoodEvent) {
        val target = event.creature.isPlayer().then("You eat", event.creature.name +" eats")
        display("$target ${event.food.name}")
        val healAmount = getHealAmount(event.food)
        EventManager.postEvent(StatChangeEvent(event.creature, event.food.name, "Health", healAmount))
        event.creature.currentLocation().removeTargetIncludingPlayerInventory(event.creature, event.food)

        if (event.food.canConsume(event)){
            event.food.consume(event)
        }
    }

    private fun getHealAmount(item: Target): Int {
        val base = item.properties.values.getInt("healAmount")
        return when  {
            item.properties.tags.has("Sliced") -> base + 1
            item.properties.tags.has("Roasted") -> (base * 1.25).toInt()
            item.properties.tags.has("Cooked") -> (base * 1.5).toInt()
            else -> base
        }
    }
}