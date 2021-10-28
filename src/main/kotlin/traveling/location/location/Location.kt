package traveling.location.location

import core.GameState
import core.events.EventManager
import core.history.display
import core.properties.CONTAINER
import core.properties.Properties
import core.properties.SIZE
import core.thing.Thing
import core.thing.activator.ActivatorManager
import core.thing.creature.CreatureManager
import core.thing.item.ItemManager
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
    private val activators: NameSearchableList<Thing> = NameSearchableList(),
    private val creatures: NameSearchableList<Thing> = NameSearchableList(),
    private val items: NameSearchableList<Thing> = NameSearchableList(),
    private val other: NameSearchableList<Thing> = NameSearchableList(),
    val properties: Properties = Properties(),
    private val recipe: LocationRecipe = locationNode.getLocationRecipe()
) : Named {
    constructor(locationNode: LocationNode) : this(
        locationNode,
        NameSearchableList<Thing>(),
        NameSearchableList<Thing>(),
        NameSearchableList<Thing>(),
        NameSearchableList<Thing>(),
        Properties()
    ){
        populateFromProtoLocation()
    }

    var weather: Weather = DEFAULT_WEATHER
    private var lastWeatherChange: Long = GameState.timeManager.getTicks()
    private var equippedItems: MutableMap<String, Thing?> = recipe.slots.associate { it.lowercase() to null }.toMutableMap()

    override val name: String
        get() = locationNode.name

    override fun toString(): String {
        return recipe.name
    }

    private fun populateFromProtoLocation() {
        properties.replaceWith(locationNode.getLocationRecipe().properties)

        if (recipe.activators.isNotEmpty()) {
            addThings(ActivatorManager.getActivatorsFromLocationThings(recipe.activators))
        }
        if (recipe.creatures.isNotEmpty()) {
            addThings(CreatureManager.getCreaturesFromLocationThings(recipe.creatures))
        }
        if (recipe.items.isNotEmpty()) {
            addThings(ItemManager.getItemsFromLocationThings(recipe.items))
        }

        changeWeatherIfEnoughTimeHasPassed()
    }

    fun hasAttachPoint(attachPoint: String): Boolean {
        return equippedItems.map { it.key.lowercase() }.contains(attachPoint.lowercase())
    }

    fun getEquippedItem(slot: String): Thing? {
        return equippedItems[slot.lowercase()]
    }

    fun getEquippedItems(): List<Thing> {
        return equippedItems.values.filterNotNull()
    }

    fun getEquippedItemMap(): Map<String, Thing?> {
        return equippedItems
    }

    fun getEquippedWeapon(): Thing? {
        return equippedItems.values.firstOrNull { it?.properties?.tags?.has("Weapon") ?: false }
    }

    fun equipItem(attachPoint: String, item: Thing) {
        if (!equippedItems.containsKey(attachPoint.lowercase())) {
            item.display("Couldn't equip $item to $attachPoint of body part ${recipe.name}. This should never happen!")
        } else {
            equippedItems[attachPoint.lowercase()] = item
            addThing(item)
        }
    }

    fun unEquip(item: Thing) {
        equippedItems.keys.forEach {
            if (equippedItems[it] == item) {
                equippedItems[it] = null
            }
        }
        removeThing(item)
    }

    fun isEmpty(): Boolean {
        return getThings().isEmpty()
    }

    fun clear() {
        creatures.clear()
        items.clear()
        activators.clear()
        other.clear()
    }

    fun addThings(things: List<Thing>) {
        things.forEach { addThing(it) }
    }

    fun addThing(thing: Thing, proxies: List<String> = listOf()) {
        when {
            thing.properties.isActivator() -> addThingTo(thing, activators, proxies)
            thing.properties.isCreature() -> addThingTo(thing, creatures, proxies)
            thing.properties.isItem() -> addThingTo(thing, items, proxies)
            else -> addThingTo(thing, other, proxies)
        }
        thing.location = locationNode
    }

    private fun addThingTo(thing: Thing, listToAddTo: NameSearchableList<Thing>, proxies: List<String>) {
        if (!getThings().contains(thing)) {
            listToAddTo.add(thing)
        }
        if (proxies.isNotEmpty()) {
            listToAddTo.addProxy(thing, proxies)
        }
    }

    fun removeThing(thing: Thing) {
        activators.remove(thing)
        creatures.remove(thing)
        items.remove(thing)
        other.remove(thing)
        thing.properties.values.clear("locationDescription")
    }

    fun removeThingIncludingPlayerInventory(source: Thing, thing: Thing) {
        if (source.inventory.exists(thing)) {
            source.inventory.remove(thing)
        } else {
            removeThing(thing)
        }
    }

    fun getThings(): NameSearchableList<Thing> {
        return (creatures + items + activators + other)
    }

    fun getThings(source: Thing): NameSearchableList<Thing> {
        return getThings().sortedBy { source.position.getDistance(it.position) }
    }

    fun getThings(name: String, source: Thing): NameSearchableList<Thing> {
        return getThingsByName(getThings(), name, source)
    }

    fun getThings(name: String): NameSearchableList<Thing> {
        return getThingsByName(getThings(), name)
    }

    fun getThingsIncludingInventories(): NameSearchableList<Thing> {
        val baseThings = getThings()
        return baseThings + baseThings.flatMap { it.inventory.getAllItems() }
    }

    fun getThingsIncludingTopLevelInventories(): NameSearchableList<Thing> {
        val baseThings = getThings()
        return baseThings + baseThings.flatMap { it.inventory.getItems() }
    }

    private fun getThingsByName(things: NameSearchableList<Thing>, name: String, source: Thing): NameSearchableList<Thing> {
        return when {
            things.existsExact(name) && things.countExact(name) == 1 -> NameSearchableList(things.get(name))
            things.existsByWholeWord(name) && things.countByWholeWord(name) == 1 -> things.getAll(name)
            else -> things.getAll(name).sortedBy { source.position.getDistance(it.position) }
        }
    }

    private fun getThingsByName(things: NameSearchableList<Thing>, name: String): NameSearchableList<Thing> {
        return when {
            things.existsExact(name) && things.countExact(name) == 1 -> NameSearchableList(things.get(name))
            things.existsByWholeWord(name) && things.countByWholeWord(name) == 1 -> things.getAll(name)
            else -> things.getAll(name)
        }
    }

    fun getThingsIncludingPlayerInventory(source: Thing): List<Thing> {
        return source.inventory.getItems() + getThings(source)
    }

    fun getThingsIncludingPlayerInventory(source: Thing, name: String): NameSearchableList<Thing> {
        return source.inventory.getItems(name) + getThings(name, source)
    }

    fun getCreaturesExcludingPlayer(source: Thing): NameSearchableList<Thing> {
        return getCreatures(source).also { it.remove(source) }
    }

    fun getCreatures(source: Thing): NameSearchableList<Thing> {
        return creatures.sortedBy { source.position.getDistance(it.position) }
    }

    fun getCreatures(): NameSearchableList<Thing> {
        return creatures
    }

    fun getCreatures(name: String, source: Thing): NameSearchableList<Thing> {
        return getThingsByName(creatures, name, source)
    }

    fun getCreatures(name: String): NameSearchableList<Thing> {
        return getThingsByName(creatures, name)
    }

    fun getActivators(name: String, source: Thing): NameSearchableList<Thing> {
        return getThingsByName(activators, name, source)
    }

    fun getActivators(name: String): NameSearchableList<Thing> {
        return getThingsByName(activators, name)
    }

    fun getActivators(source: Thing): NameSearchableList<Thing> {
        return activators.sortedBy { source.position.getDistance(it.position) }
    }

    fun getActivators(): NameSearchableList<Thing> {
        return activators
    }

    fun getItems(): NameSearchableList<Thing> {
        return items
    }

    fun getItems(source: Thing): NameSearchableList<Thing> {
        return items.sortedBy { source.position.getDistance(it.position) }
    }

    fun getItems(source: Thing, name: String): NameSearchableList<Thing> {
        return getThingsByName(items, name, source)
    }

    fun getItems(name: String): NameSearchableList<Thing> {
        return getThingsByName(items, name)
    }

    fun getItemsIncludingPlayerInventory(source: Thing): NameSearchableList<Thing> {
        return source.inventory.getItems() + getItems(source)
    }

    fun getItemsIncludingPlayerInventory(name: String, source: Thing): NameSearchableList<Thing> {
        return source.inventory.getItems(name) + getItems(source, name)
    }

    fun getOther(source: Thing): NameSearchableList<Thing> {
        return other.sortedBy { source.position.getDistance(it.position) }
    }

    fun getOther(): NameSearchableList<Thing> {
        return other
    }

    fun findThingsByTag(tag: String): NameSearchableList<Thing> {
        return getThings().filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByTag(tag: String): NameSearchableList<Thing> {
        return activators.filter { it.properties.tags.has(tag) }
    }

    fun findActivatorsByProperties(properties: Properties): NameSearchableList<Thing> {
        return activators.filter { it.properties.hasAll(properties) }
    }

    fun getAllSouls(source: Thing): List<Soul> {
        val things = mutableSetOf(source)
        things.addAll(source.inventory.getAllItems())

        getThings().forEach {
            things.add(it)
            it.inventory.getAllItems().forEach { item -> things.add(item) }
        }
        return things.map { it.soul }.toList()
    }

    fun getAllInventories(): List<Inventory> {
        return getThings().asSequence().map { it.inventory }.toList()
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
            getThings().forEach { thing ->
                val parts = thing.body.getParts()
                val condition = ConditionManager.getCondition(recipeName, parts)
                EventManager.postEvent(AddConditionEvent(thing, condition))
            }
        }
    }

    fun canHold(item: Thing): Boolean {
        return properties.tags.has(CONTAINER) && hasRoomFor(item)
                && item.properties.canBeHeldByContainerWithProperties(properties)
    }

    /**
     * How much do all of the items in this location weigh?
     */
    private fun getWeight(source: Thing): Int {
        return getItems(source).sumOf { it.getWeight() }
    }

    fun hasRoomFor(thing: Thing): Boolean {
        if (properties.values.has(SIZE)) {
            val room = properties.values.getInt(SIZE)
            return room - getWeight(thing) >= thing.getWeight()
        }
        return true
    }

    fun isSafeFor(creature: Thing): Boolean {
        //No one is hostile towards the creature
        return creatures.none { it.ai.aggroThing == creature }
    }

}