package core.gameState.dataParsing

import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item
import core.gameState.Target
import core.gameState.quests.CompleteQuestEvent
import core.gameState.quests.QuestManager
import core.gameState.quests.SetQuestStageEvent
import explore.RestrictLocationEvent
import interact.scope.*
import status.effects.AddEffectEvent
import status.effects.EffectManager
import system.*
import system.location.LocationManager
import travel.ArriveEvent

class TriggeredEvent(private val className: String, private val params: List<String> = listOf()) {

    fun applyParamValues(paramValues: Map<String, String>) : TriggeredEvent {
        val modifiedParams = mutableListOf<String>()

        params.forEach {
            modifiedParams.add(replaceParams(it, paramValues))
        }

        return TriggeredEvent(className, modifiedParams)
    }

    private fun replaceParams(line: String, paramValues: Map<String, String>): String {
        var modified = line
        paramValues.forEach {
            modified = modified.replace("$${it.key}", it.value)
        }
        return modified
    }

    fun execute(parent: Target = GameState.player) {
        when (className) {
            ArriveEvent::class.simpleName -> EventManager.postEvent(ArriveEvent(destination = LocationManager.findLocation(params[0]), method = "move"))
            MessageEvent::class.simpleName -> EventManager.postEvent(MessageEvent(params[0]))
            RemoveItemEvent::class.simpleName ->{
                val creature = getCreatureOrPlayer(1)
                val item = getItemOrParent(0, creature, parent)
                EventManager.postEvent(RemoveItemEvent(creature, item))
            }
            SpawnItemEvent::class.simpleName -> EventManager.postEvent(SpawnItemEvent(params[0], getParamInt(1), getCreatureOrNull(2)))
            RemoveScopeEvent::class.simpleName -> EventManager.postEvent(RemoveScopeEvent(getTargetOrParent(0, parent)))
            SpawnActivatorEvent::class.simpleName -> EventManager.postEvent(SpawnActivatorEvent(ActivatorManager.getActivator(params[0]), getParamBoolean(1)))
            //TODO - Add effect event is very brittle
            AddEffectEvent::class.simpleName -> EventManager.postEvent(AddEffectEvent(ScopeManager.getTarget(params[0]) as Creature, EffectManager.getEffect(params[1])))
            RestrictLocationEvent::class.simpleName -> EventManager.postEvent(RestrictLocationEvent(LocationManager.getLocationNode(params[0]), LocationManager.getLocationNode(params[1]), getParamBoolean(2)))
            SetQuestStageEvent::class.simpleName -> EventManager.postEvent(SetQuestStageEvent(QuestManager.quests.get(params[0]), getParamInt(1)))
            CompleteQuestEvent::class.simpleName -> EventManager.postEvent(CompleteQuestEvent(QuestManager.quests.get(params[0])))
        }
    }

    private fun getTargetOrParent(paramNumber: Int, parent: Target) : Target {
        val param = getParam(paramNumber, "none")
        return if (ScopeManager.targetExists(param)){
            ScopeManager.getTarget(param)
        } else {
            parent
        }
    }

    private fun getCreatureOrNull(paramNumber: Int) : Creature? {
        val param = getParam(paramNumber, "none").split(" ")
        return if (ScopeManager.creatureExists(param)){
            ScopeManager.getCreature(param)
        } else {
            null
        }
    }

    private fun getCreatureOrPlayer(paramNumber: Int) : Creature {
        val param = getParam(paramNumber, "none").split(" ")
        return if (ScopeManager.creatureExists(param)){
            ScopeManager.getCreature(param)
        } else {
            GameState.player.creature
        }
    }

    private fun getItemOrParent(paramNumber: Int, source: Creature, parent: Target) : Item {
        val param = getParam(paramNumber, "none").split(" ")
        return if (source.inventory.exists(param)){
            source.inventory.getItem(param)
        } else {
            parent as Item
        }
    }

    private fun getParam(i: Int, default: String) : String {
        if (i < params.size){
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
    private fun getParamBoolean(i: Int, default: Boolean = false) : Boolean {
        if (i < params.size){
            return params[i].toBoolean()
        }
        return default
    }
}