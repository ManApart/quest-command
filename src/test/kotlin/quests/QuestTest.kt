package quests

import org.junit.Assert
import org.junit.Test
import use.interaction.InteractEvent

class QuestTest {

    @Test
    fun questDoesNotAddDuplicateEvents() {
        val events = createEvents("quest", 5)
        val quest = createQuest(events)
        Assert.assertEquals(5, quest.getAllEvents().size)
    }

    @Test
    fun questAddsDefaultEventToEvents() {
        val quest = Quest("quest")
        quest.addEvent(StoryEvent("quest", 10, "journal", ConditionalEvents(InteractEvent::class.java)))
        Assert.assertEquals(1, quest.getAllEvents().size)
    }

    @Test
    fun lowestStageIfNoCurrentActiveStage() {
        val events = createEvents("quest", 5)
        val quest = createQuest(events)
        quest.calculateListenedForEvents()
        Assert.assertEquals(events.first(), quest.getListenedForEvents().first())
    }

    @Test
    fun activeQuestIsLowestStageAfterCurrentStage() {
        val currentStage = 2
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()
        Assert.assertEquals(currentStage + 1, quest.getListenedForEvents().first().stage)
    }

    @Test
    fun highestStageReturnedIfStageIsLastStage() {
        val currentStage = 4
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()
        Assert.assertEquals(events.last(), quest.getListenedForEvents().first())
    }

    @Test
    fun noStagesAfterCurrent() {
        val currentStage = 6
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()

        Assert.assertTrue(quest.getListenedForEvents().isEmpty())
    }

    private fun createEvents(questName: String, number: Int): List<StoryEvent> {
        return (1..number).map {
            StoryEvent(questName, it, "journal$it", ConditionalEvents(InteractEvent::class.java))
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