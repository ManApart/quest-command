package use.actions

import core.events.EventListener
import core.events.EventManager
import status.stat.HEALTH
import status.statChanged.StatChangeEvent
import use.UseEvent

class DamageCreature : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.used.properties.tags.has("Weapon") && event.target.soul.hasStat(HEALTH)
    }

    override fun execute(event: UseEvent) {
        EventManager.postEvent(StatChangeEvent(event.target, event.used.name, "Health", -event.used.getDamage()))
    }
}