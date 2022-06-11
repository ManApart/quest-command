package status.statChanged

import core.events.EventListener
import core.events.EventManager
import core.history.display
import inventory.dropItem.PlaceItemEvent
import status.CreatureDiedEvent
import status.stat.HEALTH
import traveling.scope.remove.RemoveScopeEvent

class CreatureDied : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return !event.thing.isPlayer() && event.stat.lowercase() == HEALTH.lowercase()
    }

    override fun execute(event: StatMinnedEvent) {
        event.thing.display("${event.thing.name} has died.")
        val creature = event.thing

        creature.location.getLocation().getCreatures()
            .filter { it.mind.ai.aggroThing == creature }
            .forEach { it.mind.ai.aggroThing = null }

        creature.inventory.getAllItems().forEach {
            EventManager.postEvent(PlaceItemEvent(creature, it, silent = true))
        }
        EventManager.postEvent(RemoveScopeEvent(creature))
        EventManager.postEvent(CreatureDiedEvent(creature))
    }

}