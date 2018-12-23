package interact.actions

import core.events.EventListener
import core.gameState.Item
import core.gameState.getCreature
import core.gameState.hasCreature
import core.gameState.stat.Stat
import interact.ScopeManager.getCreature
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class DamageCreature : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.source is Item && event.source.properties.tags.has("Weapon") && event.target.hasCreature() && event.target.getCreature()!!.soul.hasStat(Stat.HEALTH)
    }

    override fun execute(event: UseEvent) {
        val item = event.source as Item
        val creature = event.target.getCreature()!!
        EventManager.postEvent(StatChangeEvent(creature, event.source.name, "Health", -item.getDamage()))
    }
}