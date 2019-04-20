package status.statChanged

import core.events.EventListener
import core.gameState.Target
import core.gameState.GameState

import core.gameState.stat.HEALTH
import core.history.display
import interact.scope.remove.RemoveScopeEvent
import inventory.dropItem.TransferItemEvent
import status.CreatureDiedEvent
import system.EventManager

class CreatureDied : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return !event.target.isPlayer() && event.stat.toLowerCase() == HEALTH.toLowerCase()
    }

    override fun execute(event: StatMinnedEvent) {
        display("${event.target.name} has died.")
        val creature = event.target

        creature.inventory.getItems().forEach {
            EventManager.postEvent(TransferItemEvent(it, creature, silent = true))
        }
        EventManager.postEvent(RemoveScopeEvent(event.target))
        EventManager.postEvent(CreatureDiedEvent(event.target))
    }

}