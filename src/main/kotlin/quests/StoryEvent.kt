package quests

import core.events.Event

data class StoryEvent(
        val questName: String,
        val stage: Int,
        val journal: String,
        val triggeredEvent: ConditionalEvents<*>,
        private val repeatable: Boolean = false,
        private var availableAfter: Int = -1,
        private var availableBefore: Int = -1,
        val completesQuest: Boolean = false

) {
    var completed = false

    fun matches(event: Event): Boolean {
        return triggeredEvent.matches(event)
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

    fun execute(triggeringEvent: Event) {
        if (triggeringEvent.javaClass == triggeredEvent.triggerEvent) {
            triggeredEvent.execute(triggeringEvent)
        } else {
            println("${questName}'s event at stage $stage matched but had the wrong event type (${triggeringEvent.javaClass.simpleName}. This should not happen!")
        }
    }

}