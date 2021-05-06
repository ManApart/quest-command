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
import traveling.location.weather.DEFAULT_WEATHER
import traveling.location.weather.Weather
import traveling.location.weather.WeatherManager

class Location(
        val locationNode: LocationNode,
        private val activators: NameSearchableList<Target> = NameSearchableList(),
        private val creatures: NameSearchableList<Target> = NameSearchableList(),
        private val items: NameSearchableList<Target> = NameSearchableList(),
        private val other: NameSearchableList<Target> = NameSearchableList(),
        val properties: Properties = Properties(),
        initialize: Boolean = false,
        recipe: LocationRecipe? = null

) : Named {
    constructor(locationNode: LocationNode) : this(locationNode, NameSearchableList<Target>(), NameSearchableList<Target>(), NameSearchableList<Target>(), NameSearchableList<Target>(), Properties(), true)

    private val locationRecipe = recipe ?: locationNode.getLocationRecipe()
    var weather: Weather = DEFAULT_WEATHER

    //    var needsSaved = false
    private var lastWeatherChange: Long = GameState.timeManager.getTicks()
    private var equippedItems: MutableMap<String, Target?> = locationRecipe.slots.associate { it.lowercase() to null }.toMutableMap()

    init {
        if (initialize) {
            populateFromProtoLocation()
        }
    }

    override val name: String
        get() = locationNode.name

    override fun toString(): String {
        return locationRecipe.name
    }

    private fun populateFromProtoLocation() {
        properties.replaceWith(locationNode.getLocationRecipe().properties)

        if (locationRecipe.activators.isNotEmpty()) {
            addTargets(ActivatorManager.getActivatorsFromLocationTargets(locationRecipe.activators))
        }
        if (locationRecipe.creatures.isNotEmpty()) {
            addTargets(CreatureManager.getCreaturesFromLocationTargets(locationRecipe.creatures))
        }
        if (locationRecipe.items.isNotEmpty()) {
            addTargets(ItemManager.getItemsFromLocationTargets(locationRecipe.items))
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
            display("Couldn't equip $item to $attachPoint of body part ${locationRecipe.name}. This should never happen!")
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
        if (!getTargets().contains(target)) {
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

    fun removeTargetIncludingPlayerInventory(target: Target) {
        if (GameState.player.inventory.exists(target)) {
            GameState.player.inventory.remove(target)
        } else {
            removeTarget(target)
        }
    }

    private fun getAllTargets(): NameSearchableList<Target> {
        return (creatures + items + activators + other)
    }

    fun getTargets(source: Target = GameState.player): NameSearchableList<Target> {
        return getAllTargets().sortedBy { source.position.getDistance(it.position) }
    }

    fun getTargets(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargetsByName(getAllTargets(), name, source)
    }

    private fun getTargetsByName(targets: NameSearchableList<Target>, name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return when {
            targets.existsExact(name) && targets.countExact(name) == 1 -> NameSearchableList(targets.get(name))
            targets.existsByWholeWord(name) && targets.countByWholeWord(name) == 1 -> targets.getAll(name)
            else -> targets.getAll(name).sortedBy { source.position.getDistance(it.position) }
        }
    }

    fun getTargetsIncludingPlayerInventory(source: Target = GameState.player): List<Target> {
        return GameState.player.inventory.getItems() + getTargets(source)
    }

    fun getTargetsIncludingPlayerInventory(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems(name) + getTargets(name, source)
    }

    fun getCreaturesExcludingPlayer(source: Target = GameState.player): NameSearchableList<Target> {
        return getCreatures(source).also { it.remove(GameState.player) }
    }

    fun getCreatures(source: Target = GameState.player): NameSearchableList<Target> {
        return creatures.sortedBy { source.position.getDistance(it.position) }
    }

    fun getCreatures(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargetsByName(creatures, name, source)
    }

    fun getActivators(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargetsByName(activators, name, source)
    }

    fun getActivators(source: Target = GameState.player): NameSearchableList<Target> {
        return activators.sortedBy { source.position.getDistance(it.position) }
    }

    fun getItems(source: Target = GameState.player): NameSearchableList<Target> {
        return items.sortedBy { source.position.getDistance(it.position) }
    }

    fun getItems(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return getTargetsByName(items, name, source)
    }

    fun getItemsIncludingPlayerInventory(source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems() + getItems(source)
    }

    fun getItemsIncludingPlayerInventory(name: String, source: Target = GameState.player): NameSearchableList<Target> {
        return GameState.player.inventory.getItems(name) + getItems(name, source)
    }

    fun getOther(source: Target = GameState.player): NameSearchableList<Target> {
        return other.sortedBy { source.position.getDistance(it.position) }
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

    fun getAllSouls(): List<Soul> {
        val souls = mutableSetOf<Soul>()
        souls.addAll(GameState.player.inventory.getAllItems().map { it.soul })

        getAllTargets().forEach {
            souls.add(it.soul)
            it.inventory.getAllItems().forEach { item -> souls.add(item.soul) }
        }

        if (!souls.contains(GameState.player.soul)) {
            souls.add(GameState.player.soul)
        }
        return souls.toList()
    }

    fun getAllInventories(): List<Inventory> {
        return getAllTargets().asSequence().map { it.inventory }.toList()
    }

    fun changeWeatherIfEnoughTimeHasPassed() {
        if (lastWeatherChange == 0L || GameState.timeManager.getHoursPassed(lastWeatherChange) >= locationRecipe.weatherChangeFrequency) {
            updateWeather(WeatherManager.getWeather(locationRecipe.getWeatherName()))
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
    private fun getWeight(): Int {
        return getItems().sumOf { it.getWeight() }
    }

    fun hasRoomFor(target: Target): Boolean {
        if (properties.values.has(SIZE)) {
            val room = properties.values.getInt(SIZE)
            return room - getWeight() >= target.getWeight()
        }
        return true
    }

    fun isSafeFor(creature: Target): Boolean {
        //No one is hostile towards the creature
        return creatures.none { it.ai.aggroTarget == creature }
    }

}