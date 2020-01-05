package quests.triggerCondition

import core.GameState
import core.properties.Properties
import core.target.Target
import traveling.location.location.LocationNode
import traveling.location.location.NOWHERE_NODE
import core.utility.apply
import core.reflection.Reflections
import crafting.Recipe
import crafting.RecipeManager
import traveling.scope.ScopeManager
import status.conditions.Condition
import magic.Element
import core.DependencyInjector
import core.events.EventManager
import traveling.location.location.LocationManager

class TriggeredEvent(private val className: String, private val params: List<String> = listOf()) {
    constructor(base: TriggeredEvent, params: Map<String, String>) : this(base.className, base.params.apply(params))

    companion object {
        val eventMap = DependencyInjector.getImplementation(Reflections::class.java).getEventParsers().map { it.className() to it }.toMap()
    }

    fun execute(parent: Target = GameState.player) {
        val event = eventMap[className]?.parse(this, parent)
        if (event != null) {
            EventManager.postEvent(event)
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