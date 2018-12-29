package core.gameState.dataParsing

import core.events.Event
import core.events.getValues
import core.utility.apply

class TriggerCondition(val callingEvent: String, private val eventParams: Map<String, String> = mapOf(), private val queries: List<Query> = listOf()) {

    constructor(base: TriggerCondition, params: Map<String, String>) : this(base.callingEvent.apply(params), base.eventParams.apply(params), base.queries)

    fun matches(event: Event): Boolean {
        val params = event.getValues()
        return classMatches(event) && eventValuesMatch(params) && queries.all { it.evaluate(params) }
    }

    private fun classMatches(event: Event) = event::class.simpleName == callingEvent

    private fun eventValuesMatch(eventValues: Map<String, String>): Boolean {
        eventParams.forEach { entry ->
            val expected = entry.value
            val propertyVal = eventValues[entry.key] ?: ""

            if (expected.toLowerCase() != propertyVal.toLowerCase()) {
                return false
            }
        }
        return true
    }

}