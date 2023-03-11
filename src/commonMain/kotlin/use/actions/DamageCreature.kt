package use.actions

import core.events.EventListener
import core.events.EventManager
import explore.listen.addSoundEffect
import status.stat.HEALTH
import status.statChanged.StatChangeEvent
import use.UseEvent

class DamageCreature : EventListener<UseEvent>() {
    override suspend fun shouldExecute(event: UseEvent): Boolean {
        return event.used.properties.tags.has("Weapon") && event.usedOn.soul.hasStat(HEALTH)
    }

    override suspend fun complete(event: UseEvent) {
        EventManager.postEvent(StatChangeEvent(event.usedOn, event.used.name, "Health", -event.used.getDamage()))
        event.creature.addSoundEffect("Pain", "a sharp intake of breath")
    }
}