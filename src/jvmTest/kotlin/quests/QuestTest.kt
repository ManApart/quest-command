package quests


import kotlin.test.Test
import use.interaction.InteractEvent
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuestTest {

    @Test
    fun questDoesNotAddDuplicateEvents() {
        val events = createEvents("quest", 5)
        val quest = createQuest(events)
        assertEquals(5, quest.getAllEvents().size)
    }

    @Test
    fun questAddsDefaultEventToEvents() {
        val quest = Quest("quest")
        quest.addEvent(StoryEvent("quest", 10, "journal", ConditionalEvents(InteractEvent::class)))
        assertEquals(1, quest.getAllEvents().size)
    }

    @Test
    fun lowestStageIfNoCurrentActiveStage() {
        val events = createEvents("quest", 5)
        val quest = createQuest(events)
        quest.calculateListenedForEvents()
        assertEquals(events.first(), quest.getListenedForEvents().first())
    }

    @Test
    fun activeQuestIsLowestStageAfterCurrentStage() {
        val currentStage = 2
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()
        assertEquals(currentStage + 1, quest.getListenedForEvents().first().stage)
    }

    @Test
    fun highestStageReturnedIfStageIsLastStage() {
        val currentStage = 4
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()
        assertEquals(events.last(), quest.getListenedForEvents().first())
    }

    @Test
    fun noStagesAfterCurrent() {
        val currentStage = 6
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()

        assertTrue(quest.getListenedForEvents().isEmpty())
    }

    private fun createEvents(questName: String, number: Int): List<StoryEvent> {
        return (1..number).map {
            StoryEvent(questName, it, "journal$it", ConditionalEvents(InteractEvent::class))
        }
    }

    private fun createQuest(events: List<StoryEvent>, currentStage: Int = 0): Quest {
        val quest = Quest(events.first().questName, currentStage)
        events.forEach {
            quest.addEvent(it)
        }
        quest.initialize()
        return quest
    }
}