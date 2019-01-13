package interact.scope.spawn

import core.events.EventListener
import core.gameState.GameState
import core.history.display
import core.utility.StringFormatter
import interact.scope.ScopeManager
import inventory.pickupItem.ItemPickedUpEvent
import system.EventManager

class SpawnItem : EventListener<ItemSpawnedEvent>() {
    override fun execute(event: ItemSpawnedEvent) {
        if (event.target == null) {
            val name = StringFormatter.format(event.item.count > 1, "${event.item.count}x ${event.item.name}s", event.item.name)
            display("$name appeared.")
            event.item.location = event.targetLocation ?: GameState.player.creature.location
            ScopeManager.getScope(event.targetLocation).addTarget(event.item)
        } else {
            event.target.inventory.add(event.item)
            EventManager.postEvent(ItemPickedUpEvent(event.target, event.item, true))
        }
    }
}