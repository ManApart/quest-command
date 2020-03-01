package traveling.scope

import core.GameState
import core.events.EventManager
import core.properties.Properties
import core.target.Target
import core.utility.NameSearchableList
import core.utility.plus
import inventory.Inventory
import status.Soul
import status.conditions.AddConditionEvent
import status.conditions.ConditionManager
import traveling.location.location.HEAT
import traveling.location.location.LIGHT
import traveling.location.location.LocationNode
import traveling.location.weather.DEFAULT_WEATHER
import traveling.location.weather.Weather
import traveling.location.weather.WeatherManager

class Scope(val locationNode: LocationNode) {
    private val targets = NameSearchableList<Target>()
    val location = locationNode.getLocation()
    val properties = Properties(location.properties)
    var weather: Weather = DEFAULT_WEATHER
    private var lastWeatherChange: Long = GameState.timeManager.getTicks()

    fun isEmpty(): Boolean {
        return targets.isEmpty()
    }

    fun clear() {
        targets.clear()
    }

    fun addTarget(target: Target, proxies: List<String> = listOf()) {
        if (!targets.contains(target)) {
            targets.add(target)
        }
        if (proxies.isNotEmpty()) {
            targets.addProxy(target, proxies)
        }
    }

    fun addTargets(targets: List<Target>) {
        this.targets.addAll(targets)
    }

    fun removeTarget(target: Target) {
        targets.remove(target)
    }

    fun removeTargetIncludingPlayerInventory(target: Target) {
        if (GameState.player.inventory.exists(target)) {
            GameState.player.inventory.remove(target)
        } else {
            targets.remove(target)
        }
    }

    fun getTargets(source: Target = GameState.player): NameSearchableList<Target> {
        return targets.sortedBy { source.position.getDistance(it.position) }
    }

    fun getTargets(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return when {
            targets.existsExact(name) && targets.countExact(name) == 1 -> NameSearchableList(targets.get(name))
            targets.existsByWholeWord(name) && targets.countByWholeWord(name) == 1 -> NameSearchableList(targets.get(name))
            else -> targets.getAll(name).sortedBy { source.position.getDistance(it.position) }
        }
    }

    fun getTargetsIncludingPlayerInventory(source: Target = GameState.player): List<Target> {
        return GameState.player.inventory.getItems() + getTargets(source)
    }

    fun getTargetsIncludingPlayerInventory(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems(name) + getTargets(name, source)
    }

    fun getCreatures(source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(source).filter { it.properties.isCreature() }
    }

    fun getCreatures(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(name, source).filter { it.properties.isCreature() }
    }

    fun getActivators(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(name, source).filter { it.properties.isActivator() }
    }

    fun getActivators(source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(source).filter { it.properties.isActivator() }
    }

    fun getItems(source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(source).filter { it.properties.isItem() }
    }

    fun getItems(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargets(name, source).filter { it.properties.isItem() }
    }

    fun getItemsIncludingPlayerInventory(source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems() + getItems(source)
    }

    fun getItemsIncludingPlayerInventory(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems(name) + getItems(name, source)
    }

    fun findTargetsByTag(tag: String): NameSearchableList<Target> {
        return targets.filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByTag(tag: String): NameSearchableList<Target> {
        return findTargetsByTag(tag).filter { it.properties.isActivator() }
    }

    fun findActivatorsByProperties(properties: Properties): NameSearchableList<Target> {
        return targets.filter { it.properties.isActivator() && it.properties.hasAll(properties) }
    }

    fun getAllSouls(): List<Soul> {
        val souls = mutableListOf<Soul?>()
        souls.addAll(GameState.player.inventory.getAllItems().map { it.soul })

        targets.forEach {
            souls.add(it.soul)
            it.inventory.getAllItems().forEach { item -> souls.add(item.soul) }
        }

        if (!souls.contains(GameState.player.soul)) {
            souls.add(GameState.player.soul)
        }
        return souls.filterNotNull()
    }

    fun getAllInventories(): List<Inventory> {
        return targets.asSequence().map { it.inventory }.toList()
    }

    fun changeWeatherIfEnoughTimeHasPassed() {
        if (lastWeatherChange == 0L || GameState.timeManager.getHoursPassed(lastWeatherChange) >= location.weatherChangeFrequency) {
            updateWeather(WeatherManager.getWeather(location.getWeatherName()))
        }
    }

    fun updateWeather(newWeather: Weather) {
        lastWeatherChange = GameState.timeManager.getTicks()

        properties.values.inc(HEAT, -this.weather.properties.values.getInt(HEAT))
        properties.values.inc(LIGHT, -this.weather.properties.values.getInt(LIGHT))

        this.weather = newWeather

        properties.values.inc(HEAT, this.weather.properties.values.getInt(HEAT))
        properties.values.inc(LIGHT, this.weather.properties.values.getInt(LIGHT))
    }

    fun applyWeatherEffects() {
        val conditionRecipes = weather.conditionNames.mapNotNull { it.getOption() }
        conditionRecipes.forEach { recipeName ->
            targets.forEach { target ->
                val parts = target.body.getParts()
                val condition = ConditionManager.getCondition(recipeName, parts)
                EventManager.postEvent(AddConditionEvent(target, condition))
            }
        }
    }

}

