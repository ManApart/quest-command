package interact.actions

import core.events.EventListener
import status.StatChangeEvent
import interact.UseEvent
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item
import core.gameState.Stat
import core.utility.StringFormatter
import system.EventManager

class EatFood : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.source.properties.tags.has("Food") && event.source is Item
    }

    override fun execute(event: UseEvent) {
        val target = StringFormatter.format(event.target == GameState.player, "You eat", event.target.name +" eats")
        println("$target ${event.source}")
        EventManager.postEvent(StatChangeEvent(event.target as Creature, event.source.name, "Health", (event.source as Item).properties.values.getInt("healAmount")))
        GameState.player.inventory.items.remove(event.source)
    }
}