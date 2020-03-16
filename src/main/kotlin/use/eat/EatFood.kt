package use.eat

import core.GameState
import core.events.EventListener
import core.target.Target

import core.history.display
import core.utility.StringFormatter
import status.statChanged.StatChangeEvent
import core.events.EventManager

class EatFood : EventListener<EatFoodEvent>() {

    override fun execute(event: EatFoodEvent) {
        val target = StringFormatter.format(event.creature.isPlayer(), "You eat", event.creature.name +" eats")
        display("$target ${event.food}")
        val healAmount = getHealAmount(event.food)
        EventManager.postEvent(StatChangeEvent(event.creature, event.food.name, "Health", healAmount))
        GameState.currentLocation().removeTargetIncludingPlayerInventory(event.food)

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