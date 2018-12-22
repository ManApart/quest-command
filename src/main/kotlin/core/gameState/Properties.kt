package core.gameState

import core.utility.*

data class Properties(val tags: Tags = Tags(), val values: PropertyValues = PropertyValues(), var stats: Map<String, String> = mapOf()) {
    constructor(base: Properties) : this(Tags(base.tags.getAll()), PropertyValues(base.values), base.stats.toMap())

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

    fun applyParams(params: Map<String, String>): Properties {
        return Properties(tags.applyParams(params), values.applyParams(params), stats.applyParams(params))
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
        return mapsMatch(stats, other.stats)
    }

    private fun hasAllStats(other: Properties): Boolean {
        return mapAHasAllOfMapB(stats, other.stats)
    }
}