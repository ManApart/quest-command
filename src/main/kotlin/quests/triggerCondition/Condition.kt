package quests.triggerCondition

import core.utility.apply

class Condition(private val params: Map<String, String> = mapOf(), private val queries: List<Query> = listOf()) {

    constructor(base: Condition, params: Map<String, String>) : this(base.params.apply(params), base.queries)

    fun matches(params: Map<String, String>): Boolean {
        return eventValuesMatch(params) && queries.all { it.evaluate(params) }
    }

    private fun eventValuesMatch(eventValues: Map<String, String>): Boolean {
        params.forEach { entry ->
            val expected = entry.value
            val propertyVal = eventValues[entry.key] ?: ""

            if (expected.toLowerCase() != propertyVal.toLowerCase()) {
                return false
            }
        }
        return true
    }

}