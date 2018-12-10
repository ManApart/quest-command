package gameState.quests

import core.gameState.dataParsing.TriggerCondition
import core.gameState.quests.Quest
import core.gameState.quests.StoryEvent
import org.junit.Assert
import org.junit.Test

class QuestTest {

    @Test
    fun questDoesNotAddDuplicateEvents(){
        val events = createEvents("quest", 5)
        val quest = createQuest(events)
        Assert.assertEquals(5, quest.getAllEvents().size)
    }

    @Test
    fun questAddsDefaultEventToEvents(){
        val quest = Quest("quest")
        quest.addEvent(StoryEvent("quest", "event", 10, "journal", condition = TriggerCondition("callingEvent")))
        Assert.assertEquals(1, quest.getAllEvents().size)
    }

    @Test
    fun lowestStageIfNoCurrentActiveStage(){
        val events = createEvents("quest", 5)
        val quest = createQuest(events)
        quest.calculateListenedForEvents()
        Assert.assertEquals(events.first(), quest.getListenedForEvents().first())
    }

    @Test
    fun activeQuestIsLowestStageAfterCurrentStage(){
        val currentStage = 2
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()
        Assert.assertEquals(currentStage+1, quest.getListenedForEvents().first().stage)
    }

    @Test
    fun highestStageReturnedIfStageIsLastStage(){
        val currentStage = 4
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()
        Assert.assertEquals(events.last(), quest.getListenedForEvents().first())
    }

    @Test
    fun noStagesAfterCurrent(){
        val currentStage = 6
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateListenedForEvents()

        Assert.assertTrue(quest.getListenedForEvents().isEmpty())
    }

    private fun createEvents(questName: String, number: Int) : List<StoryEvent> {
        return (1..number).map {
            StoryEvent(questName, questName + it, it, "journal$it", condition = TriggerCondition("callingEvent"))
        }
    }

    private fun createQuest(events: List<StoryEvent>, currentStage: Int = 0) : Quest {
        val quest = Quest(events.first().name, currentStage)
        events.forEach {
            quest.addEvent(it)
        }
        quest.initialize()
        return quest
    }
}