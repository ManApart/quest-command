package processing.use

import events.StatChangeEvent
import events.UseItemEvent
import gameState.Creature
import gameState.GameState
import gameState.Stat
import processing.EventManager

class EatFood : Use {
    override fun matches(event: UseItemEvent): Boolean {
        return event.source.tags.has("Food")
    }

    override fun execute(event: UseItemEvent) {
        EventManager.postEvent(StatChangeEvent(event.target as Creature, event.source.name, Stat.StatType.HEALTH, event.source.properties.getInt("Amount")))
        GameState.player.items.remove(event.source)
    }
}