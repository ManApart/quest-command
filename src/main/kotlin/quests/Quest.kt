package quests

import core.events.Event
import core.events.EventManager
import core.utility.Named

class Quest(override val name: String, var stage: Int = 0) : Named {
    private val journalEntries = mutableListOf<String>()
    var complete = false
    var active = false
    private val storyEvents = mutableMapOf<Int, StoryEvent>()
    private val listenedForEvents = mutableListOf<StoryEvent>()

    fun addEvent(event: StoryEvent) {
        if (storyEvents.containsKey(event.stage)){
            println("WARN: Duplicate stage ${event.stage} being replaced.")
        }
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

    fun executeEvent(event: StoryEvent, triggeringEvent: Event) {
        event.execute(triggeringEvent)
        journalEntries.add(event.journal)
        event.completed = true

        if (!complete) {
            active = true
        }

        stage = event.stage
        EventManager.postEvent(QuestStageUpdatedEvent(this, stage))
        calculateListenedForEvents()
        if (event.completesQuest || listenedForEvents.isEmpty()) {
            EventManager.postEvent(CompleteQuestEvent(this))
        }
    }

    fun completeQuest() {
        complete = true
        active = false
    }

    fun hasStarted() :Boolean{
        return stage != 0 || active || complete
    }

    fun addAllEntries(journalEntries: List<String>) {
        this.journalEntries.addAll(journalEntries)
    }
}