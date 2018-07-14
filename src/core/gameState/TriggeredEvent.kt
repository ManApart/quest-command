package core.gameState

import travel.ArriveEvent
import core.events.Event
import interact.ScopeManager
import system.*

class TriggeredEvent(private val className: String, private val params: List<String>) {

    fun execute(event: Event) {
        when (className) {
            ArriveEvent::class.simpleName -> EventManager.postEvent(ArriveEvent(destination = GameState.world.findLocation(params[0].split(" ")), method = "move"))
            MessageEvent::class.simpleName -> EventManager.postEvent(MessageEvent(params[0]))
            SpawnItemEvent::class.simpleName -> EventManager.postEvent(SpawnItemEvent(params[0]))
            RemoveScopeEvent::class.simpleName -> EventManager.postEvent(RemoveScopeEvent(ScopeManager.getTarget(params[0])))
            SpawnActivatorEvent::class.simpleName -> EventManager.postEvent(SpawnActivatorEvent(ActivatorManager.getActivator(params[0])))
        }
    }
}