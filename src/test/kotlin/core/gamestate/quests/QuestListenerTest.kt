package core.gamestate.quests

import core.gameState.GameState
import core.gameState.Item
import core.gameState.Player
import core.gameState.dataParsing.TriggerCondition
import core.gameState.quests.*
import gameState.quests.QuestFakeParser
import interact.interaction.InteractEvent
import org.junit.Test
import system.DependencyInjector
import kotlin.test.assertEquals

class QuestListenerTest {


    //TODO - this runs too slowly!
    @Test
    fun questListenerIsRemovedOnExecute() {
        val quest = Quest("Test Quest")
        quest.addEvent(StoryEvent("Test Quest", "Test Event", 10, "journal", condition =  TriggerCondition("InteractEvent")))
        quest.initialize()

        val quest2 = Quest("Test Quest2")
        quest2.addEvent(StoryEvent("Test Quest2", "Test Event", 10, "journal", condition =  TriggerCondition("InteractEvent", params = mapOf("source" to "never"))))
        quest2.initialize()

        val fakeParser = QuestFakeParser(listOf(quest, quest2))
        DependencyInjector.setImplementation(QuestParser::class.java, fakeParser)
        QuestManager.reset()
        GameState.player = Player()

        val testEvent = InteractEvent(GameState.player, Item("Apple"))
        val listener = QuestListener()
        listener.execute(testEvent)
        val results = listener.getListeners()
        assertEquals(1, results.size)
        assertEquals(1, results[InteractEvent::class.java]?.size)
    }

    @Test
    fun eventListIsRemovedIfEmpty() {
        val quest = Quest("Test Quest")
        quest.addEvent(StoryEvent("Test Quest", "Test Event", 10, "journal", condition =  TriggerCondition("InteractEvent")))
        quest.initialize()

        val fakeParser = QuestFakeParser(listOf(quest))
        DependencyInjector.setImplementation(QuestParser::class.java, fakeParser)
        QuestManager.reset()
        GameState.player = Player()


        val testEvent = InteractEvent(GameState.player, Item("Apple"))
        val listener = QuestListener()
        listener.execute(testEvent)
        val results = listener.getListeners()
        assertEquals(0, results.size)
    }
}