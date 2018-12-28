package interact

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Item
import core.gameState.getCreature
import core.gameState.isPlayer
import core.history.display
import core.utility.StringFormatter
import interact.EatFoodEvent
import interact.UseEvent
import interact.scope.ScopeManager
import status.statChanged.StatChangeEvent
import system.EventManager

class EatFood : EventListener<EatFoodEvent>() {

    override fun execute(event: EatFoodEvent) {
        val target = StringFormatter.format(event.creature.isPlayer(), "You eat", event.creature.name +" eats")
        display("$target ${event.food}")
        val healAmount = getHealAmount(event.food)
        EventManager.postEvent(StatChangeEvent(event.creature, event.food.name, "Health", healAmount))
        ScopeManager.getScope().removeTargetIncludingPlayerInventory(event.food)

        if (event.food.canConsume(event)){
            event.food.consume(event)
        }
    }

    private fun getHealAmount(item: Item): Int {
        val base = item.properties.values.getInt("healAmount")
        return when  {
            item.properties.tags.has("Sliced") -> base + 1
            item.properties.tags.has("Roasted") -> (base * 1.25).toInt()
            item.properties.tags.has("Cooked") -> (base * 1.5).toInt()
            else -> base
        }
    }
}