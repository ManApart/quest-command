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

            val activators = ActivatorManager.getActivatorsFromLocationTargets(location.activators)
            activators.forEach { it.creature.location = event.destination }
            scope.addTargets(activators)

            val creatures = CreatureManager.getCreaturesFromLocationTargets(location.creatures)
            creatures.forEach { it.location = event.destination }
            scope.addTargets(creatures)

            val items = ItemManager.getItemsFromLocationTargets(location.items)
            scope.addTargets(items)
        }
    }
}