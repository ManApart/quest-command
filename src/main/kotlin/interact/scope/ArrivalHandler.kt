package interact.scope

import core.events.EventListener
import core.gameState.GameState
import system.activator.ActivatorManager
import system.creature.CreatureManager
import system.item.ItemManager
import travel.ArriveEvent

class ArrivalHandler : EventListener<ArriveEvent>() {
    override fun execute(event: ArriveEvent) {
        ScopeManager.getScope(event.origin.location).removeTarget(GameState.player)

        val location = event.destination.location.getLocation()
        val scope = ScopeManager.getScope(event.destination.location)

        if (scope.isEmpty()) {

            val activators = ActivatorManager.getActivatorsFromLocationTargets(location.activators)
            activators.forEach { it.location = event.destination.location }
            scope.addTargets(activators)

            val creatures = CreatureManager.getCreaturesFromLocationTargets(location.creatures)
            creatures.forEach { it.location = event.destination.location }
            scope.addTargets(creatures)

            val items = ItemManager.getItemsFromLocationTargets(location.items)
            scope.addTargets(items)
        }

        scope.addTarget(GameState.player, listOf("me", "self"))
    }
}