package interact.actions

import status.StatChangeEvent
import interact.UseEvent
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item
import core.gameState.Stat
import core.utility.StringFormatter
import system.EventManager

class EatFood : Action {
    override fun matches(event: UseEvent): Boolean {
        return event.source.tags.has("Food") && event.source is Item
    }

    override fun execute(event: UseEvent) {
        val target = StringFormatter.format(event.target == GameState.player, "You eat", event.target.name +" eats")
        println("$target ${event.source}")
        EventManager.postEvent(StatChangeEvent(event.target as Creature, event.source.name, Stat.StatType.HEALTH, (event.source as Item).properties.getInt("healAmount")))
        GameState.player.inventory.items.remove(event.source)
    }
}