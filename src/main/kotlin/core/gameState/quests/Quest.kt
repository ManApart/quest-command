package core.gameState.quests

import core.events.Event
import core.history.display
import core.utility.Named
import system.EventManager

class Quest(override val name: String, var activeEvent: StoryEvent, var stage: Int = 0) : Named {
    private val journalEntries = mutableListOf<String>()
    var complete = false
    private val storyEvents = mutableMapOf(activeEvent.stage to activeEvent)

    fun addEvent(event: StoryEvent) {
        storyEvents[event.stage] = event
    }

    fun calculateActiveEvent() {
        val stage = storyEvents.keys.asSequence().sorted().firstOrNull { it > stage }
                ?: storyEvents.keys.asSequence().sorted().last()
        activeEvent = storyEvents[stage]!!
    }

    fun getAllEvents(): List<StoryEvent> {
        return storyEvents.values.toList()
    }

    fun matches(event: Event, stage: StoryEvent = activeEvent): Boolean {
        return stage.matches(event)
    }

    fun execute() {
        executeEvent(activeEvent)
    }

    fun executeStage(stage: Int) {
        if (!storyEvents.containsKey(stage)){
            display("Could not find stage $stage for quest $name. This shouldn't happen!")
        } else {
            executeEvent(storyEvents[stage]!!)
        }
    }

    private fun executeEvent(event: StoryEvent) {
        //TODO - apply params?
        event.events.forEach {
            it.execute()
        }
        journalEntries.add(event.journal)

        stage = event.stage
        EventManager.postEvent(QuestStageUpdatedEvent(this, stage))
        calculateActiveEvent()
        if (stage == activeEvent.stage) {
            complete = true
            EventManager.postEvent(CompleteQuestEvent(this))
        }
    }


}