package core.gameState.dataParsing

import core.gameState.GameState
import core.gameState.Properties
import core.gameState.SetPropertiesEvent
import core.gameState.Target
import core.gameState.dataParsing.events.SpawnItemEventParser
import core.gameState.location.LocationNode
import core.gameState.location.LocationPoint
import core.gameState.location.NOWHERE_NODE
import core.gameState.quests.CompleteQuestEvent
import core.gameState.quests.QuestManager
import core.gameState.quests.SetQuestStageEvent
import core.utility.apply
import crafting.DiscoverRecipeEvent
import crafting.Recipe
import crafting.RecipeManager
import explore.RestrictLocationEvent
import interact.scope.ScopeManager
import interact.scope.remove.RemoveItemEvent
import interact.scope.remove.RemoveScopeEvent
import interact.scope.spawn.SpawnItemEvent
import status.effects.AddConditionEvent
import status.effects.Condition
import status.effects.Element
import status.effects.RemoveConditionEvent
import status.statChanged.StatChangeEvent
import system.EventManager
import system.location.LocationManager
import travel.ArriveEvent

class TriggeredEvent(private val className: String, private val params: List<String> = listOf()) {
    constructor(base: TriggeredEvent, params: Map<String, String>) : this(base.className, base.params.apply(params))

    companion object {
        val eventMap = mapOf(
                SpawnItemEvent::class.simpleName to SpawnItemEventParser()
        )
    }

    fun execute(parent: Target = GameState.player) {
        val event = eventMap[className]?.parse(this, parent)
        if (event != null) {
            EventManager.postEvent(event)
        }
        //TODO - transition to using parser classes (delete below as converted to above method)
        when (className) {
            ArriveEvent::class.simpleName -> EventManager.postEvent(ArriveEvent(destination = LocationPoint(LocationManager.getNetwork(params[0]).findLocation(params[1])), method = "move"))
            AddConditionEvent::class.simpleName -> EventManager.postEvent(AddConditionEvent(getTargetCreatureOrPlayer(parent, 0), createCondition()))
            CompleteQuestEvent::class.simpleName -> EventManager.postEvent(CompleteQuestEvent(QuestManager.quests.get(params[0])))
            DiscoverRecipeEvent::class.simpleName -> EventManager.postEvent(DiscoverRecipeEvent(GameState.player, getRecipe(0)))
            RestrictLocationEvent::class.simpleName -> EventManager.postEvent(RestrictLocationEvent(LocationManager.getNetwork(params[0]).getLocationNode(params[1]), LocationManager.getNetwork(params[2]).getLocationNode(params[3]), getParamBoolean(4)))
            RemoveConditionEvent::class.simpleName -> EventManager.postEvent(RemoveConditionEvent(getTargetCreatureOrPlayer(parent, 0), getTargetCondition(parent)))
            RemoveItemEvent::class.simpleName -> EventManager.postEvent(RemoveItemEvent(getTargetCreatureOrPlayer(parent, 0), getItemOrParent(parent, 1, getTargetCreatureOrPlayer(parent, 0))))
            RemoveScopeEvent::class.simpleName -> EventManager.postEvent(RemoveScopeEvent(getTargetOrParent(parent, 0)))
            SetPropertiesEvent::class.simpleName -> EventManager.postEvent(SetPropertiesEvent(getTargetCreatureOrPlayer(parent, 0), getProperties(1, 2, 3)))
            SetQuestStageEvent::class.simpleName -> EventManager.postEvent(SetQuestStageEvent(QuestManager.quests.get(params[0]), getParamInt(1)))
            StatChangeEvent::class.simpleName -> EventManager.postEvent(StatChangeEvent(getTargetCreatureOrPlayer(parent, 0), getParam(1, "event"), getParam(2, "none"), getParamInt(3, 0)))
        }
    }


    fun getTargetOrParent(parent: Target, paramNumber: Int): Target {
        val param = getParam(paramNumber, "none")
        return when {
            param == "\$this" -> parent
            ScopeManager.getScope().getTargets(param).isNotEmpty() -> ScopeManager.getScope().getTargets(param).first()
            else -> parent
        }
    }

    fun getLocation(parent: Target, paramNetworkNumber: Int, paramLocationNumber: Int): LocationNode? {
        val networkParam = getParam(paramNetworkNumber, "")
        val locationParam = getParam(paramLocationNumber, "")
        if (networkParam.isBlank() || locationParam.isBlank()) {
            return null
        }
        val location = LocationManager.getNetwork(networkParam).findLocation(locationParam)
        return if (location == NOWHERE_NODE) {
            null
        } else {
            location
        }
    }

    fun getTargetCreatureOrPlayer(parent: Target, paramNumber: Int, location: LocationNode? = null): Target {
        return getTargetCreature(parent, paramNumber, location) ?: GameState.player
    }

    fun getTargetCreature(parent: Target, paramNumber: Int, location: LocationNode? = null): Target? {
        val param = getParam(paramNumber, "none")
        return if (param == "\$this") {
            parent
        } else {
            ScopeManager.getScope(location).getTargets(param).firstOrNull()
        }
    }

    fun getItemOrParent(parent: Target, paramNumber: Int, source: Target): Target {
        val param = getParam(paramNumber, "none")
        return source.inventory.getItem(param) ?: parent
    }

    fun getRecipe(paramNumber: Int): Recipe {
        val param = getParam(paramNumber, "none")
        return RecipeManager.getRecipe(param)
    }

    fun getProperties(typeParam: Int, keyParam: Int, valueParam: Int): Properties {
        val properties = Properties()
        val type = getParam(typeParam, "none")
        val key = getParam(keyParam, "none")
        val value = getParam(valueParam, "none")
        when (type) {
            "values" -> properties.values.put(key, value)
            "stats" -> properties.values.put(key, value)
            "tags" -> properties.tags.add(value)
        }

        return properties
    }

    fun getParam(i: Int, default: String = ""): String {
        if (i < params.size) {
            return params[i]
        }
        return default
    }

    fun getParamInt(i: Int, default: Int = 1): Int {
        if (i < params.size) {
            return params[i].toInt()
        }
        return default
    }

    fun getParamBoolean(i: Int, default: Boolean = false): Boolean {
        if (i < params.size) {
            return params[i].toBoolean()
        }
        return default
    }

    //TODO - this should be replaced with a reference to an existing/json hard coded condition
    fun createCondition(): Condition {
        return Condition(getParam(1, ""), Element.valueOf(getParam(2, "NONE")), elementStrength = getParamInt(3))
    }

    fun getTargetCondition(parent: Target): Condition {
        val target = getTargetCreatureOrPlayer(parent, 0)
        return target.soul.getCondition(params[1])
    }
}