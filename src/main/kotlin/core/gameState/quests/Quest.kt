package core.gameState.quests

import core.events.Event
import core.history.display
import core.utility.Named
import system.EventManager

class Quest(override val name: String, var stage: Int = 0) : Named {
    private val journalEntries = mutableListOf<String>()
    var complete = false
    var active = false
    private val storyEvents = mutableMapOf<Int, StoryEvent>()
    private val listenedForEvents = mutableListOf<StoryEvent>()

    fun addEvent(event: StoryEvent) {
        storyEvents[event.stage] = event
    }

    fun initialize() {
        var stage = 0
        storyEvents.values.sortedBy { it.stage }.forEach { event ->
            event.setDefaultAvailability(stage)
            stage = event.stage
        }
        calculateListenedForEvents()
    }

    fun calculateListenedForEvents() {
        listenedForEvents.clear()
        listenedForEvents.addAll(storyEvents.values.filter { it.canBeListenedFor(stage) })
    }

    fun getAllEvents(): List<StoryEvent> {
        return storyEvents.values.toList()
    }

    fun getMatchingEvent(event: Event): StoryEvent? {
        return listenedForEvents.firstOrNull { it.matches(event) }
    }

    fun getAllJournalEntries(): List<String> {
        return journalEntries.toList()
    }

    fun getLatestJournalEntry(): String {
        return journalEntries.last()
    }

    fun getListenedForEvents(): List<StoryEvent> {
        return listenedForEvents.toList()
    }

    fun executeStage(stage: Int) {
        if (!storyEvents.containsKey(stage)) {
            display("Could not find stage $stage for quest $name. This shouldn't happen!")
        } else {
            executeEvent(storyEvents[stage]!!)
        }
    }

    fun executeEvent(event: StoryEvent) {
        event.events.forEach {
            it.execute()
        }
        journalEntries.add(event.journal)
        event.completed = true

        if (!complete) {
            active = true
        }

        stage = event.stage
        EventManager.postEvent(QuestStageUpdatedEvent(this, stage))
        calculateListenedForEvents()
        if (listenedForEvents.isEmpty()) {
            complete = true
            active = false
            EventManager.postEvent(CompleteQuestEvent(this))
        }
    }


}