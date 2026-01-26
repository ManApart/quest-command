package status.statChanged

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.filterList
import explore.listen.addSoundEffect
import inventory.dropItem.PlaceItemEvent
import status.CreatureDiedEvent
import status.stat.Attributes.HEALTH
import traveling.scope.remove.RemoveScopeEvent

class CreatureDied : EventListener<StatMinnedEvent>() {
    override suspend fun shouldExecute(event: StatMinnedEvent): Boolean {
        return !event.thing.isPlayer() && event.stat.lowercase() == HEALTH.lowercase()
    }

    override suspend fun complete(event: StatMinnedEvent) {
        event.thing.display("${event.thing.name} has died.")
        val creature = event.thing

        creature.location.getLocation().getCreatures()
            .filterList { it.mind.getAggroTarget() == creature }
            .forEach { it.mind.clearAggroTarget() }

        EventManager.removeInProgressEvents(creature)
        EventManager.postEvent(RemoveScopeEvent(creature))
        creature.inventory.getAllItems().forEach {
            EventManager.postEvent(PlaceItemEvent(creature, it, silent = true))
        }
        EventManager.postEvent(CreatureDiedEvent(creature))
        event.thing.addSoundEffect("Death", "a sharp cry, cut short", 1)
    }

}