package core.gameState

import core.utility.*

data class Properties(val tags: Tags = Tags(), val values: PropertyValues = PropertyValues(), var stats: Map<String, String> = mapOf()) {
    constructor(base: Properties, params: Map<String, String> = mapOf()) : this(
            Tags(base.tags, params),
            PropertyValues(base.values, params),
            base.stats.apply(params)
    )

    override fun toString(): String {
        return tags.toString().wrapNonEmpty("(Tags: ", ") ") +
                values.toString().wrapNonEmpty("(Values: ", ") ") +
                stats.toEmptyString().wrapNonEmpty("(Stats: ", ")")
    }

    fun matches(other: Properties): Boolean {
        return tags.matches(other.tags) && values.matches(other.values) && matchesStats(other)
    }

    fun hasAll(other: Properties): Boolean {
        return tags.hasAll(other.tags) && values.hasAll(other.values) && hasAllStats(other)
    }

    fun inherit(parent: Properties) {
        tags.inherit(parent.tags)
        values.inherit(parent.values)
        stats = stats.inheritMap(parent.stats)
    }

    fun isEmpty(): Boolean {
        return tags.isEmpty() && values.isEmpty() && stats.isEmpty()
    }

    private fun matchesStats(other: Properties): Boolean {
        return stats.matches(other.stats)
    }

    private fun hasAllStats(other: Properties): Boolean {
        return stats.hasAllOf(other.stats)
    }
}