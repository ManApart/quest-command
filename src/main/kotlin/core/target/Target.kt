package core.target

import com.fasterxml.jackson.annotation.JsonProperty
import core.GameState
import core.ai.AI
import core.ai.AIManager
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorRecipe
import core.body.Body
import core.body.BodyManager
import core.body.BodyPart
import core.body.Slot
import core.events.Event
import core.properties.*
import core.utility.*
import crafting.Recipe
import dialogue.DialogueOptions
import inventory.Inventory
import status.ProtoSoul
import status.Soul
import traveling.direction.NO_VECTOR
import traveling.direction.Vector
import traveling.location.LocationNode
import traveling.location.NOWHERE_NODE
import traveling.location.Route
import kotlin.math.max
import kotlin.math.min

open class Target(
        name: String,
        base: Target? = null,
        params: Map<String, String> = mapOf(),
        ai: AI? = null,
        aiName: String? = base?.ai?.name,
        @JsonProperty("behaviors") behaviorRecipes: MutableList<BehaviorRecipe> = base?.behaviorRecipes
                ?: mutableListOf(),
        body: String? = base?.body?.name,
        equipSlots: List<List<String>> = base?.equipSlots?.map { it.attachPoints } ?: listOf(),
        @JsonProperty("description") dynamicDescription: DialogueOptions = base?.dynamicDescription
                ?: DialogueOptions(name),
        items: List<String> = base?.inventory?.getItems()?.map { it.name } ?: listOf(),
        var location: LocationNode = base?.location ?: NOWHERE_NODE,
        val parent: Target? = null,
        @JsonProperty("soul") soulStats: ProtoSoul = ProtoSoul(),
        properties: Properties = base?.properties ?: Properties()
) : Named {

    override var name = name.apply(params)
    val ai = ai ?: AIManager.getAI(aiName, this)
    val behaviorRecipes = behaviorRecipes.asSequence().map { BehaviorRecipe(it, params) }.toMutableList()
    val body: Body = if (body == null) Body() else BodyManager.getBody(body)
    val description get() = dynamicDescription.getDialogue()
    //Why is equip slots not part of the body?
    val equipSlots = equipSlots.applyNested(params).map { Slot(it) }
    val inventory: Inventory = Inventory(items)
    val properties = Properties(properties, params)
    val soul: Soul = Soul(this, base?.soul?.getStats() ?: listOf(), soulStats.stats)
    var position = Vector()
    private val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)
    private val dynamicDescription = dynamicDescription.apply(params)
    val knownRecipes = NameSearchableList<Recipe>()

    var climbTarget: Target? = null
    var route: Route? = null

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

        return name + description + location
    }

    fun isPlayer(): Boolean {
        return this == GameState.player
    }

    fun getTopParent(): Target {
        return parent?.getTopParent() ?: this
    }

    open fun canConsume(event: Event): Boolean {
        return !isPlayer() && behaviors.any { it.evaluate(event) }
    }

    open fun consume(event: Event) {
        if (!isPlayer()) {
            behaviors.filter { it.evaluate(event) }
                    .forEach { it.execute(event, this) }
        }
    }

    fun getTotalCapacity(): Int {
        return if (soul.hasStat("Strength")) {
            soul.getCurrent("Strength") * 10
        } else {
            properties.values.getInt("Capacity", 0)
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

    fun isStackable(other: Target): Boolean {
        return name == other.name && properties.matches(other.properties)
    }

    fun getWeight(): Int {
        return properties.values.getInt("weight", 1) + inventory.getWeight()
    }

    fun copy(count: Int): Target {
        val props = Properties(properties)
        props.values.put("count", count)
        return Target(name, base = this, properties = props)
    }

    fun getPositionInLocation(part: BodyPart): Vector {
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

    fun setClimbing(climbTarget: Target) {
        properties.values.put(IS_CLIMBING, true)
        this.climbTarget = climbTarget
        properties.values.put(CAN_REST, false)
        properties.values.put(CAN_TRAVEL, false)
        properties.values.put(CAN_INTERACT, false)
    }

    fun finishClimbing() {
        properties.values.put(IS_CLIMBING, false)
        climbTarget = null
        // This should check other reasons you might not be able to rest - alternate approach is to make rest a number. At 0 you can rest, things each tick up 1 for non-rest activities?
        properties.values.put(CAN_REST, true)
        properties.values.put(CAN_TRAVEL, true)
        properties.values.put(CAN_INTERACT, true)
    }

    fun getDynamicDescription2(): DialogueOptions {
        return dynamicDescription
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

