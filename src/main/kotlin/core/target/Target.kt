package core.target

import core.GameState
import core.ai.AI
import core.ai.DumbAI
import core.ai.behavior.Behavior
import core.body.Body
import core.body.NONE
import core.body.Slot
import core.conditional.ConditionalStringPointer
import core.events.Event
import core.properties.*
import core.utility.*
import crafting.Recipe
import inventory.Inventory
import status.Soul
import traveling.location.Route
import traveling.location.location.Location
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE
import traveling.position.NO_VECTOR
import traveling.position.Vector
import kotlin.math.max
import kotlin.math.min

data class Target(
    override val name: String,
    val dynamicDescription: ConditionalStringPointer = ConditionalStringPointer(name),
    var location: LocationNode = NOWHERE_NODE,
    val parent: Target? = null,
    val ai: AI = DumbAI(),
    val body: Body = Body("None"),
    val equipSlots: List<Slot> = listOf(),
    val inventory: Inventory = Inventory(name, body),
    val properties: Properties = Properties(),
    val soul: Soul = Soul(),
    val behaviors: List<Behavior<*>> = listOf(),
    val knownRecipes: NameSearchableList<Recipe> = NameSearchableList(),
    val params: Map<String, String> = mapOf()
) : Named {
    var position = Vector()
    var climbTarget: Target? = null
    var route: Route? = null

    init {
        ai.creature = this
        soul.parent = this
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
        val body = body.copy()
        val inventory = Inventory(inventory.name, body)
        val soul = soul.copy()

        return Target(name, dynamicDescription, location, parent, ai, body, equipSlots, inventory, props, soul, behaviors, knownRecipes.toNameSearchableList(), params)
    }

    fun getPositionInLocation(part: Location): Vector {
        return body.getPositionInLocation(part, position)
    }

    fun isWithinRangeOf(creature: Target?): Boolean {
        if (creature == null
            || creature.inventory.exists(this)
            || getTopParent().location == NOWHERE_NODE
        ) {
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