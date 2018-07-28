package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.behavior.BehaviorRecipe
import core.utility.max
import system.BehaviorManager

class Item(override val name: String, override val description: String = "", val weight: Int = 0, var count: Int = 1, equipSlots: List<List<String>> = listOf(), @JsonProperty("behaviors") private val behaviorRecipes: List<BehaviorRecipe> = listOf(), override val properties: Properties = Properties()) : Target {
    val equipSlots = equipSlots.map { Slot(it) }
    val soul = Soul(this)
    val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)

    init {
        soul.addStats(properties.stats)
    }

    override fun toString(): String {
        return name
    }

    fun copy(): Item {
        return Item(name, description, weight, 1, equipSlots.map { it.bodyParts.map { it } }, behaviorRecipes, properties)
    }

    fun evaluateAndExecute(event: Event) {
        behaviors.forEach { it.evaluateAndExecute(this, event) }
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

    fun findSlot(body: Body, bodyPart: String): Slot? {
        return equipSlots.firstOrNull { it.contains(bodyPart) && body.canEquip(it) }
    }

    fun getDamage(): Int {
        val chop = properties.values.getInt("chopDamage", 0)
        val stab = properties.values.getInt("stabDamage", 0)
        val slash = properties.values.getInt("slashDamage", 0)
        return max(chop, stab, slash)
    }

    fun isStackable(other: Item) : Boolean {
        return name == other.name && properties.matches(other.properties)
    }

}