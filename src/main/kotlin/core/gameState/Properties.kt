package core.gameState

import combat.battle.Distances.BOW_RANGE
import combat.battle.Distances.DAGGER_RANGE
import combat.battle.Distances.MIN_RANGE
import combat.battle.Distances.SPEAR_RANGE
import combat.battle.Distances.SWORD_RANGE
import core.utility.wrapNonEmpty

data class Properties(val tags: Tags = Tags(), val values: Values = Values(), var stats: Values =  Values()) {
    constructor(base: Properties, params: Map<String, String> = mapOf()) : this(
            Tags(base.tags, params),
            Values(base.values, params),
            Values(base.stats, params)
    )

    override fun toString(): String {
        return tags.toString().wrapNonEmpty("(Tags: ", ") ") +
                values.toString().wrapNonEmpty("(Values: ", ") ") +
                stats.toString().wrapNonEmpty("(Stats: ", ") ")
    }

    fun matches(other: Properties): Boolean {
        return tags.matches(other.tags) && values.matches(other.values) && matchesStats(other)
    }

    fun hasAll(other: Properties): Boolean {
        return tags.hasAll(other.tags) && values.hasAll(other.values) && stats.hasAll(other.stats)
    }

    fun isEmpty(): Boolean {
        return tags.isEmpty() && values.isEmpty() && stats.isEmpty()
    }

    fun setFrom(other: Properties) {
        stats.setFrom(other.stats)
        tags.addAll(other.tags)
        values.setFrom(other.values)
    }

    private fun matchesStats(other: Properties): Boolean {
        return stats.matches(other.stats)
    }

    fun getCount(): Int {
        return stats.getInt("count", 1)
    }

    fun incCount(amount: Int) {
        stats.inc("count", amount)
    }

    fun isItem() : Boolean {
        return tags.has("Item")
    }

    fun isActivator() : Boolean {
        return tags.has("Activator")
    }

    fun isCreature() : Boolean {
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

    fun getRange() : Int {
        return when {
            tags.hasAny(Tags(listOf("Small", "Short"))) -> DAGGER_RANGE
            tags.has("Medium") -> SWORD_RANGE
            tags.hasAny(Tags(listOf("Large", "Long"))) -> SPEAR_RANGE
            tags.has("Ranged") -> BOW_RANGE
            else -> MIN_RANGE
        }
    }

    fun getDefense(type: String): Int {
        return if (stats.has(type)) {
            stats.getInt(type)
        } else {
            stats.getInt("defense", 0)
        }
    }


}