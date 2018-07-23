package core.gameState

import core.utility.mapsMatch

class Properties(val tags: Tags = Tags(), val values: PropertyValues = PropertyValues(), val stats: Map<String, String> = HashMap()) {

    fun matches(other: Properties) : Boolean {
        return tags.matches(other.tags) && values.matches(other.values) && matchesStats(other)
    }

    private fun matchesStats(other: Properties): Boolean {
        return mapsMatch(stats, other.stats)
    }
}