package interact.actions

import core.events.EventListener
import status.statChanged.StatChangeEvent
import interact.UseEvent
import core.gameState.Creature
import core.gameState.Item
import core.gameState.getCreature
import core.gameState.hasCreature
import system.EventManager

class DamageCreature : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return hasCreature(event.target) && event.source is Item && event.source.properties.tags.has("Weapon")
    }

    override fun execute(event: UseEvent) {
        val item = event.source as Item
        val creature = getCreature(event.target)!!
        EventManager.postEvent(StatChangeEvent(creature, event.source.name, "Health", -item.getDamage()))
    }
}