package core.properties

import core.properties.TagKey.ITEM
import core.thing.activator.ACTIVATOR_TAG
import core.utility.wrapNonEmpty
import traveling.position.Distances.BOW_RANGE
import traveling.position.Distances.DAGGER_RANGE
import traveling.position.Distances.MIN_RANGE
import traveling.position.Distances.SPEAR_RANGE
import traveling.position.Distances.SWORD_RANGE


data class Properties(val values: Values = Values(), val tags: Tags = Tags()) {
    constructor(tags: Tags) : this(Values(), tags)
    constructor(vararg tags: String) : this(Values(), Tags(*tags))
    constructor(vararg values: Pair<String, String>) : this(Values(*values), Tags())
    constructor(base: Properties, params: Map<String, String> = mapOf()) : this(
            Values(base.values, params),
            Tags(base.tags, params)
    )

    fun replaceWith(other: Properties) {
        tags.clear()
        values.clear()
        overrideWith(other)
    }

    fun overrideWith(other: Properties) {
        tags.addAll(other.tags)
        other.values.getAll().forEach { (key, value) -> values.put(key, value) }
    }

    override fun toString(): String {
        return tags.toString().wrapNonEmpty("(Tags: ", ") ") +
                values.toString().wrapNonEmpty("(Values: ", ") ")
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

    fun isNotEmpty(): Boolean {
        return !isEmpty()
    }

    fun setFrom(other: Properties) {
        tags.addAll(other.tags)
        values.setFrom(other.values)
    }

    fun getCount(): Int {
        return values.getInt(COUNT, 1)
    }

    fun incCount(amount: Int) {
        values.inc(COUNT, amount)
    }

    fun isItem(): Boolean {
        return tags.has(ITEM)
    }

    fun isActivator(): Boolean {
        return tags.has(ACTIVATOR_TAG)
    }

    fun isCreature(): Boolean {
        return tags.has(TagKey.CREATURE)
    }

    fun canBeHeldByContainerWithProperties(containerProperties: Properties): Boolean {
        val acceptedTypes = containerProperties.values.getList(CAN_HOLD).toMutableList()
        return if (acceptedTypes.isEmpty()) {
            true
        } else {
            tags.hasAny(Tags(acceptedTypes))
        }
    }

    fun getRange(): Int {
        return when {
            tags.hasAny(Tags(SMALL, SHORT)) -> DAGGER_RANGE
            tags.has(MEDIUM) -> SWORD_RANGE
            tags.hasAny(Tags(LARGE, LONG)) -> SPEAR_RANGE
            tags.has(RANGED) -> BOW_RANGE
            else -> MIN_RANGE
        }
    }

    fun getDefense(type: String): Int {
        return if (values.has(type)) {
            values.getInt(type)
        } else {
            values.getInt(DEFENSE, 0)
        }
    }


}
