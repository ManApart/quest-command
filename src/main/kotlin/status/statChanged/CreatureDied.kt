package status.statChanged

import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.stat.Stat
import core.history.display
import inventory.dropItem.PlaceItemEvent
import status.CreatureDiedEvent
import system.EventManager
import interact.scope.RemoveScopeEvent

class CreatureDied : EventListener<StatMinnedEvent>() {
    override fun shouldExecute(event: StatMinnedEvent): Boolean {
        return event.target != GameState.player.creature && event.target is Creature && event.stat.toLowerCase() == Stat.HEALTH.toLowerCase()
    }

    override fun execute(event: StatMinnedEvent) {
        display("${event.target.name} has died.")
        val creature = event.target as Creature

        creature.inventory.getAllItems().forEach {
            EventManager.postEvent(PlaceItemEvent(creature, it, true))
        }
        EventManager.postEvent(RemoveScopeEvent(event.target))
        EventManager.postEvent(CreatureDiedEvent(event.target))
    }

}