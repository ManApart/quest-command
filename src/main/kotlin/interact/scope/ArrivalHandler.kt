package interact.scope

import core.events.EventListener
import system.ActivatorManager
import system.CreatureManager
import system.ItemManager
import travel.ArriveEvent

class ArrivalHandler : EventListener<ArriveEvent>() {
    override fun execute(event: ArriveEvent) {
        ScopeManager.resetTargets()
        val location = event.destination.getLocation()
        ScopeManager.addTargets(ActivatorManager.getActivatorsFromLocationTargets(location.activators))
        ScopeManager.addTargets(CreatureManager.getCreaturesFromLocationTargets(location.creatures))
        ScopeManager.addTargets(ItemManager.getItemsFromLocationTargets(location.items))
    }
}