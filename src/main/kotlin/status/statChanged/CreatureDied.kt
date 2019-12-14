package status.statChanged

import core.events.EventListener
import status.stat.HEALTH
import core.history.display
import traveling.scope.remove.RemoveScopeEvent
import inventory.dropItem.TransferItemEvent
import status.CreatureDiedEvent
import core.events.EventManager

class CreatureDied : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return !event.target.isPlayer() && event.stat.toLowerCase() == HEALTH.toLowerCase()
    }

    override fun execute(event: StatMinnedEvent) {
        display("${event.target.name} has died.")
        val creature = event.target

        creature.inventory.getItems().forEach {
            EventManager.postEvent(TransferItemEvent(creature, it, creature, silent = true))
        }
        EventManager.postEvent(RemoveScopeEvent(event.target))
        EventManager.postEvent(CreatureDiedEvent(event.target))
    }

}