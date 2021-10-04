package traveling.location.location

import core.GameState
import core.events.EventManager
import core.history.display
import core.properties.CONTAINER
import core.properties.Properties
import core.properties.SIZE
import core.target.Target
import core.target.activator.ActivatorManager
import core.target.creature.CreatureManager
import core.target.item.ItemManager
import core.utility.NameSearchableList
import core.utility.Named
import core.utility.plus
import inventory.Inventory
import status.Soul
import status.conditions.AddConditionEvent
import status.conditions.ConditionManager
import traveling.location.network.LocationNode
import traveling.location.weather.DEFAULT_WEATHER
import traveling.location.weather.Weather
import traveling.location.weather.WeatherManager

data class Location(
    val locationNode: LocationNode,
    private val activators: NameSearchableList<Target> = NameSearchableList(),
    private val creatures: NameSearchableList<Target> = NameSearchableList(),
    private val items: NameSearchableList<Target> = NameSearchableList(),
    private val other: NameSearchableList<Target> = NameSearchableList(),
    val properties: Properties = Properties(),
    private val recipe: LocationRecipe = locationNode.getLocationRecipe()
) : Named {
    constructor(locationNode: LocationNode) : this(
        locationNode,
        NameSearchableList<Target>(),
        NameSearchableList<Target>(),
        NameSearchableList<Target>(),
        NameSearchableList<Target>(),
        Properties()
    ){
        populateFromProtoLocation()
    }

    var weather: Weather = DEFAULT_WEATHER
    private var lastWeatherChange: Long = GameState.timeManager.getTicks()
    private var equippedItems: MutableMap<String, Target?> = recipe.slots.associate { it.lowercase() to null }.toMutableMap()

    override val name: String
        get() = locationNode.name

    override fun toString(): String {
        return recipe.name
    }

    private fun populateFromProtoLocation() {
        properties.replaceWith(locationNode.getLocationRecipe().properties)

        if (recipe.activators.isNotEmpty()) {
            addTargets(ActivatorManager.getActivatorsFromLocationTargets(recipe.activators))
        }
        if (recipe.creatures.isNotEmpty()) {
            addTargets(CreatureManager.getCreaturesFromLocationTargets(recipe.creatures))
        }
        if (recipe.items.isNotEmpty()) {
            addTargets(ItemManager.getItemsFromLocationTargets(recipe.items))
        }

        changeWeatherIfEnoughTimeHasPassed()
    }

    fun hasAttachPoint(attachPoint: String): Boolean {
        return equippedItems.map { it.key.lowercase() }.contains(attachPoint.lowercase())
    }

    fun getEquippedItem(slot: String): Target? {
        return equippedItems[slot.lowercase()]
    }

    fun getEquippedItems(): List<Target> {
        return equippedItems.values.filterNotNull()
    }

    fun getEquippedItemMap(): Map<String, Target?> {
        return equippedItems
    }

    fun getEquippedWeapon(): Target? {
        return equippedItems.values.firstOrNull { it?.properties?.tags?.has("Weapon") ?: false }
    }

    fun equipItem(attachPoint: String, item: Target) {
        if (!equippedItems.containsKey(attachPoint.lowercase())) {
            display("Couldn't equip $item to $attachPoint of body part ${recipe.name}. This should never happen!")
        } else {
            equippedItems[attachPoint.lowercase()] = item
            addTarget(item)
        }
    }

    fun unEquip(item: Target) {
        equippedItems.keys.forEach {
            if (equippedItems[it] == item) {
                equippedItems[it] = null
            }
        }
        removeTarget(item)
    }

    fun isEmpty(): Boolean {
        return getAllTargets().isEmpty()
    }

    fun clear() {
        creatures.clear()
        items.clear()
        activators.clear()
        other.clear()
    }

    fun addTargets(targets: List<Target>) {
        targets.forEach { addTarget(it) }
    }

    fun addTarget(target: Target, proxies: List<String> = listOf()) {
        when {
            target.properties.isActivator() -> addTargetTo(target, activators, proxies)
            target.properties.isCreature() -> addTargetTo(target, creatures, proxies)
            target.properties.isItem() -> addTargetTo(target, items, proxies)
            else -> addTargetTo(target, other, proxies)
        }
        target.location = locationNode
    }

    private fun addTargetTo(target: Target, listToAddTo: NameSearchableList<Target>, proxies: List<String>) {
        if (!getAllTargets().contains(target)) {
            listToAddTo.add(target)
        }
        if (proxies.isNotEmpty()) {
            listToAddTo.addProxy(target, proxies)
        }
    }

    fun removeTarget(target: Target) {
        activators.remove(target)
        creatures.remove(target)
        items.remove(target)
        other.remove(target)
        target.properties.values.clear("locationDescription")
    }

    fun removeTargetIncludingPlayerInventory(source: Target, target: Target) {
        if (source.inventory.exists(target)) {
            source.inventory.remove(target)
        } else {
            removeTarget(target)
        }
    }

    private fun getAllTargets(): NameSearchableList<Target> {
        return (creatures + items + activators + other)
    }

    //TODO - rename get all targets and just use that
    fun getTargets(): NameSearchableList<Target> {
        return getAllTargets()
    }

    fun getTargets(source: Target): NameSearchableList<Target> {
        return getAllTargets().sortedBy { source.position.getDistance(it.position) }
    }

    fun getTargets(name: String, source: Target): NameSearchableList<Target> {
        return getTargetsByName(getAllTargets(), name, source)
    }

    fun getTargets(name: String): NameSearchableList<Target> {
        return getTargetsByName(getAllTargets(), name)
    }

    private fun getTargetsByName(targets: NameSearchableList<Target>, name: String, source: Target): NameSearchableList<Target> {
        return when {
            targets.existsExact(name) && targets.countExact(name) == 1 -> NameSearchableList(targets.get(name))
            targets.existsByWholeWord(name) && targets.countByWholeWord(name) == 1 -> targets.getAll(name)
            else -> targets.getAll(name).sortedBy { source.position.getDistance(it.position) }
        }
    }

    private fun getTargetsByName(targets: NameSearchableList<Target>, name: String): NameSearchableList<Target> {
        return when {
            targets.existsExact(name) && targets.countExact(name) == 1 -> NameSearchableList(targets.get(name))
            targets.existsByWholeWord(name) && targets.countByWholeWord(name) == 1 -> targets.getAll(name)
            else -> targets.getAll(name)
        }
    }

    fun getTargetsIncludingPlayerInventory(source: Target): List<Target> {
        return source.inventory.getItems() + getTargets(source)
    }

    fun getTargetsIncludingPlayerInventory(source: Target, name: String): NameSearchableList<Target> {
        return source.inventory.getItems(name) + getTargets(name, source)
    }

    fun getCreaturesExcludingPlayer(source: Target): NameSearchableList<Target> {
        return getCreatures(source).also { it.remove(source) }
    }

    fun getCreatures(source: Target): NameSearchableList<Target> {
        return creatures.sortedBy { source.position.getDistance(it.position) }
    }

    fun getCreatures(): NameSearchableList<Target> {
        return creatures
    }

    fun getCreatures(name: String, source: Target): NameSearchableList<Target> {
        return getTargetsByName(creatures, name, source)
    }

    fun getCreatures(name: String): NameSearchableList<Target> {
        return getTargetsByName(creatures, name)
    }

    fun getActivators(name: String, source: Target): NameSearchableList<Target> {
        return getTargetsByName(activators, name, source)
    }

    fun getActivators(name: String): NameSearchableList<Target> {
        return getTargetsByName(activators, name)
    }

    fun getActivators(source: Target): NameSearchableList<Target> {
        return activators.sortedBy { source.position.getDistance(it.position) }
    }

    fun getActivators(): NameSearchableList<Target> {
        return activators
    }

    fun getItems(): NameSearchableList<Target> {
        return items
    }

    fun getItems(source: Target): NameSearchableList<Target> {
        return items.sortedBy { source.position.getDistance(it.position) }
    }

    fun getItems(source: Target, name: String): NameSearchableList<Target> {
        return getTargetsByName(items, name, source)
    }

    fun getItems(name: String): NameSearchableList<Target> {
        return getTargetsByName(items, name)
    }

    fun getItemsIncludingPlayerInventory(source: Target): NameSearchableList<Target> {
        return source.inventory.getItems() + getItems(source)
    }

    fun getItemsIncludingPlayerInventory(name: String, source: Target): NameSearchableList<Target> {
        return source.inventory.getItems(name) + getItems(source, name)
    }

    fun getOther(source: Target): NameSearchableList<Target> {
        return other.sortedBy { source.position.getDistance(it.position) }
    }

    fun getOther(): NameSearchableList<Target> {
        return other
    }

    fun findTargetsByTag(tag: String): NameSearchableList<Target> {
        return getAllTargets().filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByTag(tag: String): NameSearchableList<Target> {
        return activators.filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByProperties(properties: Properties): NameSearchableList<Target> {
        return activators.filter { it.properties.hasAll(properties) }
    }

    fun getAllSouls(source: Target): List<Soul> {
        val targets = mutableSetOf(source)
        targets.addAll(source.inventory.getAllItems())

        getAllTargets().forEach {
            targets.add(it)
            it.inventory.getAllItems().forEach { item -> targets.add(item) }
        }
        return targets.map { it.soul }.toList()
    }

    fun getAllInventories(): List<Inventory> {
        return getAllTargets().asSequence().map { it.inventory }.toList()
    }

    fun changeWeatherIfEnoughTimeHasPassed() {
        if (lastWeatherChange == 0L || GameState.timeManager.getHoursPassed(lastWeatherChange) >= recipe.weatherChangeFrequency) {
            updateWeather(WeatherManager.getWeather(recipe.getWeatherName()))
        }
    }

    fun updateWeather(newWeather: Weather) {
        lastWeatherChange = GameState.timeManager.getTicks()
        this.weather = newWeather
    }

    fun applyWeatherEffects() {
        val conditionRecipes = weather.conditionNames
        conditionRecipes.forEach { recipeName ->
            getAllTargets().forEach { target ->
                val parts = target.body.getParts()
                val condition = ConditionManager.getCondition(recipeName, parts)
                EventManager.postEvent(AddConditionEvent(target, condition))
            }
        }
    }

    fun canHold(item: Target): Boolean {
        return properties.tags.has(CONTAINER) && hasRoomFor(item)
                && item.properties.canBeHeldByContainerWithProperties(properties)
    }

    /**
     * How much do all of the items in this location weigh?
     */
    private fun getWeight(source: Target): Int {
        return getItems(source).sumOf { it.getWeight() }
    }

    fun hasRoomFor(target: Target): Boolean {
        if (properties.values.has(SIZE)) {
            val room = properties.values.getInt(SIZE)
            return room - getWeight(target) >= target.getWeight()
        }
        return true
    }

    fun isSafeFor(creature: Target): Boolean {
        //No one is hostile towards the creature
        return creatures.none { it.ai.aggroTarget == creature }
    }

    fun getLightLevel(): Int {
        return 0
    }

}