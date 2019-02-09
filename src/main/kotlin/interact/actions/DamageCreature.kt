package interact.actions

import core.events.EventListener
import core.gameState.Item
import core.gameState.getCreature
import core.gameState.hasCreature
import core.gameState.stat.HEALTH
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class DamageCreature : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.used is Item
                && event.used.properties.tags.has("Weapon")
                && event.target.hasCreature()
                && event.target.getCreature()!!.soul.hasStat(HEALTH)
    }

    override fun execute(event: UseEvent) {
        val item = event.used as Item
        val creature = event.target.getCreature()!!
        EventManager.postEvent(StatChangeEvent(creature, event.used.name, "Health", -item.getDamage()))
    }
}