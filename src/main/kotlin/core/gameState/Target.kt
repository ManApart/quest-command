package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.ai.AI
import core.gameState.behavior.BehaviorRecipe
import core.gameState.body.Body
import core.gameState.body.BodyPart
import core.gameState.body.Slot
import core.gameState.location.LocationNode
import core.gameState.location.NOWHERE_NODE
import core.utility.Named
import core.utility.apply
import core.utility.applyNested
import core.utility.max
import dialogue.DialogueOptions
import system.ai.AIManager
import system.behavior.BehaviorManager
import system.body.BodyManager

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
        properties: Properties = base?.properties ?: Properties()
) : Named {

    override val name = name.apply(params)
    val ai = ai ?: AIManager.getAI(aiName, this)
    val behaviorRecipes = behaviorRecipes.asSequence().map { BehaviorRecipe(it, params) }.toMutableList()
    val body: Body = if (body == null) Body() else BodyManager.getBody(body)
    val description get() = dynamicDescription.getDialogue()
    val equipSlots = equipSlots.applyNested(params).map { Slot(it) }
    val inventory: Inventory = Inventory(items)
    val properties = Properties(properties, params)
    val soul = Soul(this)
    var position = Vector()
    private val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)
    private val dynamicDescription = dynamicDescription.apply(params)

    init {
        soul.addStats(this.properties.stats.getAll())
    }

    override fun toString(): String {
        return getDisplayName()
    }

    fun getDisplayName(): String {
        val locationDescription = properties.values.getString("locationDescription")
        return name + if (locationDescription.isBlank()) {
            ""
        } else {
            " $locationDescription"
        }
    }

    fun isPlayer(): Boolean {
        return this == GameState.player || this == GameState.player
    }

    fun getTopParent(): Target {
        return parent?.getTopParent() ?: this
    }

    open fun canConsume(event: Event): Boolean {
        return behaviors.any { it.evaluate(event) }
    }

    open fun consume(event: Event) {
        behaviors.filter { it.evaluate(event) }
                .forEach { it.execute(event, this) }
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
        return inventory.getWeight() / getTotalCapacity().toFloat()
    }

    /**
     * Return the inverse (1-percent) how encumbered the creature is, as a percent from 0-1.
     * Useful if multiplying this by some other stat. At 0% encumbered the stat is at 100%. At 100% encumbered the stat is 0%.
     */
    fun getEncumbranceInverted(): Float {
        return 1 - (inventory.getWeight() / getTotalCapacity().toFloat())
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
        return properties.stats.getInt("weight", 1) + inventory.getWeight()
    }

    fun copy(count: Int): Target {
        val props = Properties(properties)
        props.stats.put("count", count)
        return Target(name, base = this, properties = props)
    }

    fun getPositionInLocation(part: BodyPart) : Vector{
        return body.getPositionInLocation(part, position)
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

