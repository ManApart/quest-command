package core.gameState

import core.utility.*

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

    fun inherit(parent: Properties) {
        tags.inherit(parent.tags)
        values.inherit(parent.values)
        stats.inherit(parent.stats)
    }

    fun isEmpty(): Boolean {
        return tags.isEmpty() && values.isEmpty() && stats.isEmpty()
    }

    private fun matchesStats(other: Properties): Boolean {
        return stats.matches(other.stats)
    }

}