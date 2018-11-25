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
        val quest = Quest("quest", StoryEvent("quest", "event", 10, "journal", TriggerCondition("callingEvent")))
        Assert.assertEquals(1, quest.getAllEvents().size)
    }

    @Test
    fun lowestStageIfNoCurrentActiveStage(){
        val events = createEvents("quest", 5)
        val quest = createQuest(events)
        quest.calculateActiveEvent()
        Assert.assertEquals(events.first(), quest.activeEvent)
    }

    @Test
    fun activeQuestIsLowestStageAfterCurrentStage(){
        val currentStage = 2
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateActiveEvent()
        Assert.assertEquals(currentStage+1, quest.activeEvent.stage)
    }

    @Test
    fun highestStageReturnedIfStageIsLastStage(){
        val currentStage = 4
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateActiveEvent()
        Assert.assertEquals(events.last(), quest.activeEvent)
    }

    @Test
    fun highestStageReturnedIfNoStagesAfterCurrent(){
        val currentStage = 6
        val events = createEvents("quest", 5)
        val quest = createQuest(events, currentStage)
        quest.calculateActiveEvent()
        Assert.assertEquals(events.last(), quest.activeEvent)
    }

    private fun createEvents(questName: String, number: Int) : List<StoryEvent> {
        return (1..number).map {
            StoryEvent(questName, questName + it, it, "journal$it", TriggerCondition("callingEvent"))
        }
    }

    private fun createQuest(events: List<StoryEvent>, currentStage: Int = 0) : Quest {
        val quest = Quest(events.first().name, events.first(), currentStage)
        events.forEach {
            quest.addEvent(it)
        }
        return quest
    }
}