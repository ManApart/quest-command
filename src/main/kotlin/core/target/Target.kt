package core.target

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import core.GameState
import core.ai.AI
import core.ai.AIManager
import core.ai.DumbAI
import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorRecipe
import core.body.Body
import core.body.BodyManager
import core.body.Slot
import core.conditional.ConditionalStringPointer
import core.events.Event
import core.properties.*
import core.target.item.ItemManager
import core.utility.*
import crafting.Recipe
import inventory.Inventory
import status.ProtoSoul
import status.Soul
import traveling.position.NO_VECTOR
import traveling.position.Vector
import traveling.location.Route
import traveling.location.location.Location
import traveling.location.location.LocationNode
import traveling.location.location.NOWHERE_NODE
import kotlin.math.max
import kotlin.math.min

open class Target(
        name: String,
        base: Target? = null,
        private val params: Map<String, String> = mapOf(),
        ai: AI? = null,
        aiName: String? = base?.ai?.name,
        @JsonProperty("behaviors") val behaviorRecipes: List<BehaviorRecipe> = base?.behaviorRecipes ?: listOf(),
        body: Body? = null,
        bodyName: String? = base?.body?.name,
        equipSlots: List<List<String>> = base?.equipSlots?.map { it.attachPoints } ?: listOf(),
        @JsonProperty("description") private val dynamicDescription: ConditionalStringPointer = base?.dynamicDescription
                ?: ConditionalStringPointer(name),
        items: List<String> = base?.inventory?.getItems()?.map { it.name } ?: listOf(),
        var location: LocationNode = base?.location ?: NOWHERE_NODE,
        val parent: Target? = null,
        @JsonProperty("soul") soulStats: ProtoSoul = ProtoSoul(),
        properties: Properties = base?.properties ?: Properties()
) : Named {

    override val name = name.apply(params)
    val ai = discernAI(ai, aiName)

    val body: Body = getBody(body, base?.body, bodyName)

    //Equip slots are the list of slots that this item can be equipped to. They are compared with a body that this item may be equipped to
    val equipSlots = equipSlots.applyNested(params).map { Slot(it) }
    val inventory: Inventory = Inventory(name, this.body)
    val properties = Properties(properties, params)
    val soul: Soul = Soul(this, base?.soul?.getStats() ?: listOf(), soulStats.stats)
    var position = Vector()
    @JsonIgnore val behaviors: List<Behavior<*>> = base?.behaviors ?: behaviorRecipes.asSequence().map { BehaviorManager.getBehavior(it) }.toMutableList()
    val knownRecipes = NameSearchableList<Recipe>()

    var climbTarget: Target? = null
    var route: Route? = null

    init {
        if (items.isNotEmpty()) {
            inventory.addAll(ItemManager.getItems(items))
        }
    }

    private fun discernAI(ai: AI?, aiName: String?): AI {
        return when {
            ai != null -> ai
            aiName != null -> AIManager.getAI(aiName, this)
            else -> DumbAI(this)
        }
    }

    private fun getBody(body: Body?, baseBody: Body?, bodyName: String?): Body {
        return when {
            body != null -> body
            baseBody != null -> Body(baseBody)
            bodyName != null -> BodyManager.getBody(bodyName)
            else -> Body()
        }
    }

    override fun toString(): String {
        return getDisplayName()
    }

    fun getDisplayName(): String {
        val locationDescription = properties.values.getString("locationDescription")
        val description = if (locationDescription.isBlank()) {
            ""
        } else {
            " $locationDescription"
        }

        val location = if (position == NO_VECTOR) {
            ""
        } else {
            " ($position)"
        }

        val count = if (properties.getCount() != 1) {
            "" + properties.getCount() + "x "
        } else {
            ""
        }

        return count + name + description + location
    }

    fun isPlayer(): Boolean {
        return this == GameState.player
    }

    fun getTopParent(): Target {
        return parent?.getTopParent() ?: this
    }

    fun canConsume(event: Event): Boolean {
        return !isPlayer() && behaviors.any { it.matches(event) }
    }

    fun consume(event: Event) {
        if (!isPlayer()) {
            behaviors.filter { it.matches(event) }
                    .forEach { it.execute(event) }
        }
    }

    fun getTotalCapacity(): Int {
        return if (soul.hasStat("Strength")) {
            soul.getCurrent("Strength") * 10
        } else {
            properties.values.getInt("Size", 0)
        }
    }

    /**
     * Return how encumbered the creature is, as a percent from 0-1
     */
    fun getEncumbrance(): Float {
        val soulEncumbrance = soul.parent.properties.values.getInt(ENCUMBRANCE, 0) / 100f
        val physicalEncumbrance = inventory.getWeight() / getTotalCapacity().toFloat()
        return max(0f, min(1f, soulEncumbrance + physicalEncumbrance))
    }

    fun getEncumbrancePhysicalOnly(): Float {
        val physicalEncumbrance = inventory.getWeight() / getTotalCapacity().toFloat()
        return max(0f, min(1f, physicalEncumbrance))
    }

    /**
     * Return the inverse (1-percent) how encumbered the creature is, as a percent from 0-1.
     * Useful if multiplying this by some other stat. At 0% encumbered the stat is at 100%. At 100% encumbered the stat is 0%.
     */
    fun getEncumbranceInverted(): Float {
        return 1 - getEncumbrance()
    }

    fun canEquipTo(body: Body): Boolean {
        return equipSlots.any { slot ->
            body.canEquip(slot)
        }
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

    fun isStackable(other: Target): Boolean {
        return name == other.name && properties.matches(other.properties)
    }

    fun getWeight(): Int {
        return properties.values.getInt(WEIGHT, 1) + inventory.getWeight()
    }

    fun copy(count: Int): Target {
        val props = Properties(properties)
        props.values.put(COUNT, count)
        return Target(name, base = this, properties = props)
    }

    fun getPositionInLocation(part: Location): Vector {
        return body.getPositionInLocation(part, position)
    }

    fun isWithinRangeOf(creature: Target?): Boolean {
        if (creature == null
                || creature.inventory.exists(this)
                || getTopParent().location == NOWHERE_NODE) {
            return true
        }

        val range = creature.body.getRange()
        val centerOfCreature = creature.position + Vector(z = creature.body.getSize().z / 2)
        val distance = centerOfCreature.getDistance(position)
        return getTopParent().location == creature.getTopParent().location && range >= distance
    }

    fun canReach(position: Vector): Boolean {
        val range = body.getRange()
        val centerOfCreature = position + Vector(z = body.getSize().z / 2)
        val distance = centerOfCreature.getDistance(position)
        return range >= distance
    }

    fun setClimbing(climbTarget: Target) {
        properties.values.put(IS_CLIMBING, true)
        this.climbTarget = climbTarget
    }

    fun finishClimbing() {
        properties.values.put(IS_CLIMBING, false)
        climbTarget = null
    }

    fun getDescription(): String {
        return dynamicDescription.getOption().apply(params)
    }

    fun getDescriptionWithOptions(): ConditionalStringPointer {
        return dynamicDescription
    }

    fun isSafe(): Boolean {
        return location.getLocation().isSafeFor(this) && !properties.values.getBoolean(IS_CLIMBING)
    }

    fun canInteract(): Boolean {
        return !properties.values.getBoolean(IS_CLIMBING)
    }

}

fun targetsToString(targets: List<Target>): String {
    val targetCounts = HashMap<String, Int>()
    targets.forEach {
        val count = it.properties.getCount()
        targetCounts[it.getDisplayName()] = targetCounts[it.getDisplayName()]?.plus(count) ?: count
    }

    return targetCounts.entries.joinToString(", ") {
        if (it.value == 1) {
            it.key
        } else {
            "${it.value}x ${it.key}"
        }
    }
}

