package core.gameState.dataParsing

import core.gameState.*
import core.gameState.Target
import core.gameState.location.LocationNode
import core.gameState.quests.CompleteQuestEvent
import core.gameState.quests.QuestManager
import core.gameState.quests.SetQuestStageEvent
import core.utility.apply
import explore.RestrictLocationEvent
import interact.scope.*
import status.effects.AddEffectEvent
import status.effects.EffectManager
import system.*
import system.location.LocationManager
import travel.ArriveEvent

class TriggeredEvent(private val className: String, private val params: List<String> = listOf()) {

    constructor(base: TriggeredEvent, params: Map<String, String>) : this(base.className, base.params.apply(params))

    fun execute(parent: Target = GameState.player) {
        when (className) {
            ArriveEvent::class.simpleName -> EventManager.postEvent(ArriveEvent(destination = LocationManager.findLocation(params[0]), method = "move"))
            //TODO - Add effect event is very brittle
            AddEffectEvent::class.simpleName -> EventManager.postEvent(AddEffectEvent(ScopeManager.getScope().getTarget(params[0]) as Creature, EffectManager.getEffect(params[1])))
            CompleteQuestEvent::class.simpleName -> EventManager.postEvent(CompleteQuestEvent(QuestManager.quests.get(params[0])))
            MessageEvent::class.simpleName -> EventManager.postEvent(MessageEvent(params[0]))
            RestrictLocationEvent::class.simpleName -> EventManager.postEvent(RestrictLocationEvent(LocationManager.getLocationNode(params[0]), LocationManager.getLocationNode(params[1]), getParamBoolean(2)))
            RemoveItemEvent::class.simpleName -> EventManager.postEvent(RemoveItemEvent(getTargetCreature(0), getItemOrParent(1, getTargetCreature(0), parent)))
            RemoveScopeEvent::class.simpleName -> EventManager.postEvent(RemoveScopeEvent(getTargetOrParent(0, parent)))
            SetQuestStageEvent::class.simpleName -> EventManager.postEvent(SetQuestStageEvent(QuestManager.quests.get(params[0]), getParamInt(1)))
            SpawnActivatorEvent::class.simpleName -> EventManager.postEvent(SpawnActivatorEvent(ActivatorManager.getActivator(params[0]), getParamBoolean(1)))
            SpawnItemEvent::class.simpleName -> EventManager.postEvent(SpawnItemEvent(params[0], getParamInt(1), getTargetCreature(2, getLocation(3)), getLocation(3)))
        }
    }

    private fun getTargetOrParent(paramNumber: Int, parent: Target): Target {
        val param = getParam(paramNumber, "none")
        return if (ScopeManager.getScope().targetExists(param)) {
            ScopeManager.getScope().getTarget(param)!!
        } else {
            parent
        }
    }

    private fun getLocation(paramNumber: Int): LocationNode? {
        val param = getParam(paramNumber, "none")
        val location = LocationManager.findLocation(param)
        return if (location == LocationManager.NOWHERE_NODE) {
            null
        } else {
            location
        }
    }

    private fun getTargetCreature(paramNumber: Int, location: LocationNode? = null): Creature {
        val param = getParam(paramNumber, "none")
        return ScopeManager.getScope(location).getTarget(param)?.getCreature() ?: GameState.player.creature
    }

    private fun getItemOrParent(paramNumber: Int, source: Creature, parent: Target): Item {
        val param = getParam(paramNumber, "none")
        return source.inventory.getItem(param) ?: parent as Item
    }

    private fun getParam(i: Int, default: String): String {
        if (i < params.size) {
            return params[i]
        }
        return default
    }

    private fun getParamInt(i: Int, default: Int = 1): Int {
        if (i < params.size) {
            return params[i].toInt()
        }
        return default
    }

    private fun getParamBoolean(i: Int, default: Boolean = false): Boolean {
        if (i < params.size) {
            return params[i].toBoolean()
        }
        return default
    }
}