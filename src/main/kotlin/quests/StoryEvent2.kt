package quests

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import quests.triggerCondition.TriggerCondition
import quests.triggerCondition.TriggeredEvent

class StoryEvent2(
        @JsonProperty("quest") val questName: String,
        val name: String,
        val stage: Int,
        val journal: String,
        private val triggerEvent: TriggeredEvent2<*>,
        private val repeatable: Boolean = false,
        private var availableAfter: Int = -1,
        private var availableBefore: Int = -1,
) {
    var completed = false

    fun matches(event: Event): Boolean {
        return triggerEvent.matches(event)
    }

    fun canBeListenedFor(stage: Int): Boolean {
        return (!completed || repeatable) && availableAfter <= stage && stage <= availableBefore
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