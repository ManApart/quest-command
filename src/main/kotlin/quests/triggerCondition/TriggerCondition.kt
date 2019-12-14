package quests.triggerCondition

import com.fasterxml.jackson.annotation.JsonCreator
import core.events.Event
import core.events.getValues
import core.utility.apply

class TriggerCondition(val callingEvent: String, private val condition: Condition = Condition()) {
    @JsonCreator constructor(callingEvent: String, params: Map<String, String> = mapOf(), queries: List<Query> = listOf()) : this(callingEvent, Condition(params, queries))
    constructor(base: TriggerCondition, params: Map<String, String>) : this(base.callingEvent.apply(params), Condition(base.condition, params))

    fun matches(event: Event): Boolean {
        return classMatches(event) && condition.matches(event.getValues())
    }

    private fun classMatches(event: Event) = event::class.simpleName == callingEvent

}