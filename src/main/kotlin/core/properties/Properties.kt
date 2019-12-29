package core.properties

import combat.battle.Distances.BOW_RANGE
import combat.battle.Distances.DAGGER_RANGE
import combat.battle.Distances.MIN_RANGE
import combat.battle.Distances.SPEAR_RANGE
import combat.battle.Distances.SWORD_RANGE
import core.utility.wrapNonEmpty
import system.persistance.Persistable

data class Properties(val values: Values = Values(), val tags: Tags = Tags()) {

    constructor(tags: Tags) : this(Values(), tags)
    constructor(base: Properties, params: Map<String, String> = mapOf()) : this(
            Values(base.values, params),
            Tags(base.tags, params)
    )

    override fun toString(): String {
        return tags.toString().wrapNonEmpty("(Tags: ", ") ") +
                values.toString().wrapNonEmpty("(Values: ", ") ")
    }

    override fun getVersion(): Int {
        return 0
    }

    override fun getPersisted(): Map<String, Any> {
        val data = mutableMapOf<String, Any>()
        data["values"] = values.getAll()
        data["tags"] = tags.getAll()
        return data
    }

    override fun applyData(data: Map<String, Any>) {
        values.clear()
        (data["values"] as Map<*, *>).forEach { values.put(it.key as String, it.value as String) }

        tags.clear()
        (data["tags"] as List<*>).forEach { tags.add(it as String) }

    }

    fun matches(other: Properties): Boolean {
        return tags.matches(other.tags) && values.matches(other.values)
    }

    fun hasAll(other: Properties): Boolean {
        return tags.hasAll(other.tags) && values.hasAll(other.values)
    }

    fun isEmpty(): Boolean {
        return tags.isEmpty() && values.isEmpty()
    }

    fun setFrom(other: Properties) {
        tags.addAll(other.tags)
        values.setFrom(other.values)
    }

    fun getCount(): Int {
        return values.getInt("count", 1)
    }

    fun incCount(amount: Int) {
        values.inc("count", amount)
    }

    fun isItem(): Boolean {
        return tags.has("Item")
    }

    fun isActivator(): Boolean {
        return tags.has("Activator")
    }

    fun isCreature(): Boolean {
        return tags.has("Creature")
    }

    fun canBeHeldByContainerWithProperties(containerProperties: Properties): Boolean {
        val acceptedTypes = containerProperties.values.getList("CanHold")
        return if (acceptedTypes.isEmpty()) {
            true
        } else {
            tags.hasAny(Tags(acceptedTypes))
        }
    }

    fun getRange(): Int {
        return when {
            tags.hasAny(Tags(listOf("Small", "Short"))) -> DAGGER_RANGE
            tags.has("Medium") -> SWORD_RANGE
            tags.hasAny(Tags(listOf("Large", "Long"))) -> SPEAR_RANGE
            tags.has("Ranged") -> BOW_RANGE
            else -> MIN_RANGE
        }
    }

    fun getDefense(type: String): Int {
        return if (values.has(type)) {
            values.getInt(type)
        } else {
            values.getInt("defense", 0)
        }
    }


}