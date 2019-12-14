package core.gamestate.quests

import core.GameState
import core.target.Player
import core.target.Target
import quests.triggerCondition.TriggerCondition
import quests.*
import quests.QuestFakeParser
import use.interaction.InteractEvent
import org.junit.BeforeClass
import org.junit.Test
import system.BodyFakeParser
import core.DependencyInjector
import core.body.BodyManager
import core.body.BodyParser
import system.location.LocationFakeParser
import traveling.location.LocationManager
import traveling.location.LocationParser
import kotlin.test.assertEquals

class QuestListenerTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            val bodyParser = BodyFakeParser()
            DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
            BodyManager.reset()

            val locationParser = LocationFakeParser()
            DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
            LocationManager.reset()

            GameState.player = Player()
        }
    }


    @Test
    fun questListenerIsRemovedOnExecute() {
        val quest = Quest("Test Quest")
        quest.addEvent(StoryEvent("Test Quest", "Test Event", 10, "journal", condition = TriggerCondition("InteractEvent")))
        quest.initialize()

        val quest2 = Quest("Test Quest2")
        quest2.addEvent(StoryEvent("Test Quest2", "Test Event", 10, "journal", condition = TriggerCondition("InteractEvent", params = mapOf("source" to "never"))))
        quest2.initialize()

        val questParser = QuestFakeParser(listOf(quest, quest2))
        DependencyInjector.setImplementation(QuestParser::class.java, questParser)
        QuestManager.reset()

        val testEvent = InteractEvent(GameState.player, Target("Apple"))
        val listener = QuestListener()
        listener.execute(testEvent)

        val results = listener.getListeners()
        assertEquals(1, results.size)
        assertEquals(1, results[InteractEvent::class.java.simpleName]?.size)
    }

    @Test
    fun eventListIsRemovedIfEmpty() {
        val quest = Quest("Test Quest")
        quest.addEvent(StoryEvent("Test Quest", "Test Event", 10, "journal", condition = TriggerCondition("InteractEvent")))
        quest.initialize()

        val fakeParser = QuestFakeParser(listOf(quest))
        DependencyInjector.setImplementation(QuestParser::class.java, fakeParser)
        QuestManager.reset()

        val testEvent = InteractEvent(GameState.player, Target("Apple"))
        val listener = QuestListener()
        listener.execute(testEvent)
        val results = listener.getListeners()
        assertEquals(0, results.size)
    }
}