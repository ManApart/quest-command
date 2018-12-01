package core.gameState.quests

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.dataParsing.Query
import core.gameState.dataParsing.TriggerCondition
import core.gameState.dataParsing.TriggeredEvent

class StoryEvent(
        @JsonProperty("quest") val questName: String,
        val name: String,
        val stage: Int,
        val journal: String,
        val condition: TriggerCondition,
        private val queries: List<Query> = listOf(),
        val events: List<TriggeredEvent> = listOf()
) {
    fun matches(event: Event): Boolean {
        return condition.matches(event) && queries.all { it.evaluate() }
    }
}