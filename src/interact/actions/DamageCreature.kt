package interact.actions

import status.StatChangeEvent
import interact.UseEvent
import core.gameState.Creature
import core.gameState.Item
import core.gameState.Stat
import system.EventManager

class DamageCreature : Action {
    override fun matches(event: UseEvent): Boolean {
        return event.target is Creature && event.source is Item && event.source.tags.has("Weapon")
    }

    override fun execute(event: UseEvent) {
        val item = event.source as Item
        EventManager.postEvent(StatChangeEvent(event.target as Creature, event.source.name, "Health", -item.properties.getInt("Damage", 1)))
    }
}