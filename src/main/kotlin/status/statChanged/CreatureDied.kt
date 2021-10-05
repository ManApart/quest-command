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
        return !event.target.isPlayer() && event.stat.lowercase() == HEALTH.lowercase()
    }

    override fun execute(event: StatMinnedEvent) {
        display("${event.target.name} has died.")
        val creature = event.target

        creature.location.getLocation().getCreatures().forEach {
            if (it.ai.aggroTarget == creature) {
                it.ai.aggroTarget = null
            }
        }

        creature.inventory.getItems().forEach {
//            EventManager.postEvent(TransferItemEvent(creature, it, creature, silent = true))
            EventManager.postEvent(PlaceItemEvent(creature, it, silent = true))
        }
        EventManager.postEvent(RemoveScopeEvent(creature))
        EventManager.postEvent(CreatureDiedEvent(creature))
    }

}