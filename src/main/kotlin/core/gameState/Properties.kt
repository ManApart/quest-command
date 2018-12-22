package core.gameState

import core.utility.inheritMap
import core.utility.mapAHasAllOfMapB
import core.utility.mapsMatch

data class Properties(val tags: Tags = Tags(), val values: PropertyValues = PropertyValues(), var stats: Map<String, String> = mapOf()) {
    constructor(base: Properties) : this(Tags(base.tags.getAll()), PropertyValues(base.values), base.stats.toMap())

    fun matches(other: Properties): Boolean {
        return tags.matches(other.tags) && values.matches(other.values) && matchesStats(other)
    }

    fun hasAll(other: Properties): Boolean {
        return tags.hasAll(other.tags) && values.hasAll(other.values) && hasAllStats(other)
    }

    private fun matchesStats(other: Properties): Boolean {
        return mapsMatch(stats, other.stats)
    }

    private fun hasAllStats(other: Properties): Boolean {
        return mapAHasAllOfMapB(stats, other.stats)
    }

    fun applyParams(params: Map<String, String>): Properties {
        return Properties(tags.applyParams(params), values.applyParams(params), core.utility.applyParams(stats, params))
    }

    fun inherit(parent: Properties) {
        tags.inherit(parent.tags)
        values.inherit(parent.values)
        stats = inheritMap(parent.stats, stats)
    }

    fun isEmpty(): Boolean {
        return tags.isEmpty() && values.isEmpty() && stats.isEmpty()
    }
}