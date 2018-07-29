package core.gameState

import core.utility.inheritMap
import core.utility.mapsMatch

class Properties(val tags: Tags = Tags(), val values: PropertyValues = PropertyValues(), var stats: Map<String, String> = mapOf()) {

    fun matches(other: Properties) : Boolean {
        return tags.matches(other.tags) && values.matches(other.values) && matchesStats(other)
    }

    private fun matchesStats(other: Properties): Boolean {
        return mapsMatch(stats, other.stats)
    }

    fun applyParams(params: Map<String, String>) : Properties {
        return Properties(tags.applyParams(params), values.applyParams(params), core.utility.applyParams(stats, params))
    }

    fun inherit(parent: Properties) {
        tags.inherit(parent.tags)
        values.inherit(parent.values)
        stats = inheritMap(parent.stats, stats)
    }
}