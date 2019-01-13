package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.behavior.BehaviorRecipe
import core.gameState.body.Body
import core.gameState.body.Slot
import core.gameState.location.LocationNode
import core.utility.apply
import core.utility.applyNested
import core.utility.max
import system.BehaviorManager
import system.ItemManager
import system.location.LocationManager

class Item(
        name: String,
        description: String = "",
        params: Map<String, String> = mapOf(),
        private val weight: Int = 1,
        var count: Int = 1,
        equipSlots: List<List<String>> = listOf(),
        @JsonProperty("behaviors") behaviorRecipes: MutableList<BehaviorRecipe> = mutableListOf(),
        properties: Properties = Properties()
) : Target {

    constructor(base: Item, params: Map<String, String> = mapOf()) : this(
            base.name,
            base.description,
            params,
            base.weight,
            base.count,
            base.equipSlots.map { it.attachPoints },
            base.behaviorRecipes,
            base.properties
    )

    constructor(base: Item, count: Int) : this(base) {
        this.count = count
    }


    override val name = name.apply(params)
    override val description = description.apply(params)
    override val properties: Properties = Properties(properties, params)
    val equipSlots = equipSlots.applyNested(params).map { Slot(it) }
    val soul = Soul(this)
    private val behaviorRecipes = behaviorRecipes.asSequence().map { BehaviorRecipe(it, params) }.toMutableList()
    private val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)
    override val inventory = Inventory()
    override var location: LocationNode = LocationManager.NOWHERE_NODE

    init {
        soul.addStats(this.properties.stats.getAll())
    }

    override fun toString(): String {
        return name
    }

    fun evaluate(event: Event): Boolean {
        return behaviors.any { it.evaluate(event) }
    }

    fun evaluateAndExecute(event: Event) {
        behaviors.filter { it.evaluate(event) }
                .forEach { it.execute(event, this) }
    }

    fun canEquipTo(body: Body): Boolean {
        equipSlots.forEach { slot ->
            if (body.canEquip(slot)) {
                return true
            }
        }
        return false
    }

    fun getEquippedSlot(body: Body): Slot {
        return equipSlots.first { it.itemIsEquipped(this, body) }
    }

    fun findSlot(body: Body, attachPoint: String): Slot? {
        return equipSlots.firstOrNull { it.contains(attachPoint) && body.canEquip(it) }
    }

    fun getDamage(): Int {
        val chop = properties.values.getInt("chopDamage", 0)
        val stab = properties.values.getInt("stabDamage", 0)
        val slash = properties.values.getInt("slashDamage", 0)
        return max(chop, stab, slash)
    }

    fun isStackable(other: Item): Boolean {
        return name == other.name && properties.matches(other.properties)
    }

    fun getTaggedItemName(): String {
        val orig = ItemManager.getItem(name)
        val newTags = properties.tags.getAll() - orig.properties.tags.getAll()
        return if (newTags.isNotEmpty()) {
            newTags.joinToString(" ") + " " + name
        } else {
            name
        }
    }

    fun getDefense(type: String): Int {
        return if (properties.stats.has(type)) {
            properties.stats.getInt(type)
        } else {
            properties.stats.getInt("defense", 0)
        }
    }

    fun getWeight(): Int {
        return weight + inventory.getWeight()
    }

    fun canBeHeldByContainerWithProperties(containerProperties: Properties): Boolean {
        val acceptedTypes = containerProperties.values.getList("CanHold")
        return if (acceptedTypes.isEmpty()) {
            true
        } else {
            properties.tags.hasAny(Tags(acceptedTypes))
        }
    }

}