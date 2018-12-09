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
        private val repeatable: Boolean = false,
        private var availableAfter: Int = -1,
        private var availableBefore: Int = -1,
        val condition: TriggerCondition,
        private val queries: List<Query> = listOf(),
        val events: List<TriggeredEvent> = listOf()
) {
    var completed = false

    fun matches(event: Event): Boolean {
        return condition.matches(event) && queries.all { it.evaluate() }
    }

    fun canBeListenedFor(stage: Int): Boolean {
        return (!completed || repeatable) && availableAfter >= stage && availableBefore <= stage
    }

    fun setDefaultAvailability(previousStage: Int) {
        if (availableBefore == -1) {
            availableBefore = previousStage
        }
        if (availableAfter == -1) {
            availableAfter = previousStage
        }
    }

}