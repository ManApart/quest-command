package interact.actions

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Item
import core.gameState.getCreature
import core.utility.StringFormatter
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class EatFood : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.source.properties.tags.has("Food") && event.source is Item && event.target.properties.tags.has("Creature")
    }

    override fun execute(event: UseEvent) {
        val target = StringFormatter.format(event.target == GameState.player, "You eat", event.target.name +" eats")
        println("$target ${event.source}")
        val creature = getCreature(event.target)!!
        EventManager.postEvent(StatChangeEvent(creature, event.source.name, "Health", (event.source as Item).properties.values.getInt("healAmount")))
        creature.inventory.remove(event.source)
    }
}