package core.gameState

import travel.ArriveEvent
import core.events.Event
import interact.ScopeManager
import status.effects.AddEffectEvent
import status.effects.EffectManager
import system.*

class TriggeredEvent(private val className: String, private val params: List<String>) {

    fun execute() {
        when (className) {
            ArriveEvent::class.simpleName -> EventManager.postEvent(ArriveEvent(destination = GameState.world.findLocation(params[0].split(" ")), method = "move"))
            MessageEvent::class.simpleName -> EventManager.postEvent(MessageEvent(params[0]))
            SpawnItemEvent::class.simpleName -> EventManager.postEvent(SpawnItemEvent(params[0], getParamInt(1)))
            RemoveScopeEvent::class.simpleName -> EventManager.postEvent(RemoveScopeEvent(ScopeManager.getTarget(params[0])))
            SpawnActivatorEvent::class.simpleName -> EventManager.postEvent(SpawnActivatorEvent(ActivatorManager.getActivator(params[0])))
            //TODO - very brittle
            AddEffectEvent::class.simpleName -> EventManager.postEvent(AddEffectEvent(ScopeManager.getTarget(params[0]) as Creature, EffectManager.getEffect(params[1])))
        }
    }

    private fun getParam(i: Int, default: String) : String {
        if (params.size < i){
            return params[i]
        }
        return default
    }

    private fun getParamInt(i: Int, default: Int = 1) : Int {
        if (i < params.size){
            return params[i].toInt()
        }
        return default
    }
}