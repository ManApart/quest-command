package interact.scope

import core.events.EventListener
import core.gameState.GameState
import system.ActivatorManager
import system.CreatureManager
import system.ItemManager
import travel.ArriveEvent

class ArrivalHandler : EventListener<ArriveEvent>() {
    override fun execute(event: ArriveEvent) {
        ScopeManager.getScope(event.origin).removeTarget(GameState.player)

        val location = event.destination.getLocation()
        val scope = ScopeManager.getScope(event.destination)
        if (scope.isEmpty()) {
            scope.addTarget(GameState.player, listOf("me", "self"))
            scope.addTargets(ActivatorManager.getActivatorsFromLocationTargets(location.activators))
            scope.addTargets(CreatureManager.getCreaturesFromLocationTargets(location.creatures))
            scope.addTargets(ItemManager.getItemsFromLocationTargets(location.items))
        }
    }
}