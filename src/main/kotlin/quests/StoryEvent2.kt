package quests

import core.events.Event

class StoryEvent2(
        val questName: String,
        val stage: Int,
        val journal: String,
        private val triggerEvent: TriggeredEvent2<*>,
        private val repeatable: Boolean = false,
        private var availableAfter: Int = -1,
        private var availableBefore: Int = -1,
        private var completesQuest: Boolean = false

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