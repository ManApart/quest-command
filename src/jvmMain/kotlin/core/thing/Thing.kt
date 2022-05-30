package core.thing

import core.GameState
import core.ai.AI
import core.ai.DumbAI
import core.ai.behavior.Behavior
import core.ai.knowledge.Mind
import core.body.Body
import core.body.Slot
import core.events.Event
import core.properties.*
import core.utility.Named
import core.utility.clamp
import core.utility.max
import explore.listen.getSound
import inventory.Inventory
import status.Soul
import status.stat.PERCEPTION
import status.stat.SNEAK
import system.debug.DebugType
import traveling.location.Route
import traveling.location.location.Location
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE
import traveling.position.NO_VECTOR
import traveling.position.Vector
import traveling.scope.MAX_LIGHT
import traveling.scope.getLightLevel
import traveling.scope.getLightSources
import kotlin.math.max
import kotlin.math.min

//TODO `==` includes things like inventory, which can break using this as a key in a map
data class Thing(
    override val name: String,
    val description: String = name,
    var location: LocationNode = NOWHERE_NODE,
    val parent: Thing? = null,
    val mind: Mind = Mind(),
    val body: Body = Body("None"),
    val equipSlots: List<Slot> = listOf(),
    val inventory: Inventory = Inventory(name, body),
    val properties: Properties = Properties(),
    val soul: Soul = Soul(),
    val behaviors: List<Behavior<*>> = listOf(),
    val params: Map<String, String> = mapOf()
) : Named {
    var position = Vector()
    var climbThing: Thing? = null
    var route: Route? = null

    init {
        mind.updateCreature(this)
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
            " $position"
        }

        val count = if (properties.getCount() != 1) {
            "" + properties.getCount() + "x "
        } else {
            ""
        }

        return count + name + description + location
    }

    fun isPlayer(): Boolean {
        return GameState.getPlayer(this) != null
    }

    fun getTopParent(): Thing {
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

    fun findSlotFromPart(body: Body, partName: String): Slot? {
        return body.getPartOrNull(partName)?.let { part ->
            equipSlots.firstOrNull { slot ->
                slot.attachPoints.all { part.hasAttachPoint(it) }
            }
        }
    }

    fun getDamage(): Int {
        val chop = properties.values.getInt("chopDamage", 0)
        val stab = properties.values.getInt("stabDamage", 0)
        val slash = properties.values.getInt("slashDamage", 0)
        return max(chop, stab, slash)
    }

    fun isStackable(other: Thing): Boolean {
        return name == other.name && properties.matches(other.properties)
    }

    fun getWeight(): Int {
        return properties.values.getInt(WEIGHT, 1) + inventory.getWeight()
    }

    fun copy(count: Int): Thing {
        val props = Properties(properties)
        props.values.put(COUNT, count)
        val body = body.copy()
        val inventory = Inventory(inventory.name, body)
        val soul = soul.copy()

        return Thing(name, description, location, parent, mind, body, equipSlots, inventory, props, soul, behaviors, params)
    }

    fun getPositionInLocation(part: Location): Vector {
        return body.getPositionInLocation(part, position)
    }

    fun isWithinRangeOf(creature: Thing?): Boolean {
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

    fun setClimbing(climbThing: Thing) {
        properties.values.put(IS_CLIMBING, true)
        this.climbThing = climbThing
    }

    fun finishClimbing() {
        properties.values.put(IS_CLIMBING, false)
        climbThing = null
    }

    fun isSafe(): Boolean {
        return location.getLocation().isSafeFor(this) && !properties.values.getBoolean(IS_CLIMBING)
    }

    fun canInteract(): Boolean {
        return !properties.values.getBoolean(IS_CLIMBING)
    }

    fun currentLocation(): Location {
        return location.getLocation()
    }

    fun getSize(): Int {
        return properties.values.getInt("size", body.getSize().getDistance())
    }

    fun getClarity(): Int {
        return (soul.getCurrent(PERCEPTION) + 5).clamp(0, 100)
    }

    fun getStealthLevel(observer: Thing, lightSources: List<Thing> = location.getLocation().getLightSources()): Int {
        val size = min(getSize(), 50)
        val soundLevel = getSound(observer)?.strength ?: 0
        val sneak = soul.getCurrent(SNEAK)
        val darkLevel = (MAX_LIGHT - location.getLocation().getLightLevel(this, lightSources)).clamp(0, 10)
        val adjustedDark = (darkLevel * darkLevel)
        return (sneak + adjustedDark - size - soundLevel).clamp(0, 100)
    }

    fun perceives(other: Thing): Boolean {
        if (GameState.getDebugBoolean(DebugType.CLARITY) || this === other) return true
        return location == other.location && getClarity() >= other.getStealthLevel(this) || inventory.exists(other)
    }

}

fun List<Thing>.perceivedBy(source: Thing): List<Thing> {
    if (GameState.getDebugBoolean(DebugType.CLARITY)) return this
    val clarity = source.getClarity()
    val lightSources = source.location.getLocation().getLightSources()

    return filter { other ->
        source === other || clarity >= other.getStealthLevel(source, lightSources) || source.inventory.exists(other)
    }
}

fun List<Thing>.toThingString(): String {
    val thingCounts = HashMap<String, Int>()
    this.forEach {
        val count = it.properties.getCount()
        thingCounts[it.getDisplayName()] = thingCounts[it.getDisplayName()]?.plus(count) ?: count
    }

    return thingCounts.entries.joinToString(", ") {
        if (it.value == 1) {
            it.key
        } else {
            "${it.value}x ${it.key}"
        }
    }
}