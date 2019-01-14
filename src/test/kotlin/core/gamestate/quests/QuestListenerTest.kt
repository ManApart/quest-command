package core.gamestate.quests

import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item
import core.gameState.Player
import core.gameState.body.ProtoBody
import core.gameState.dataParsing.TriggerCondition
import core.gameState.location.LocationNode
import core.gameState.quests.*
import core.utility.NameSearchableList
import gameState.quests.QuestFakeParser
import interact.interaction.InteractEvent
import org.junit.BeforeClass
import org.junit.Test
import system.BodyFakeParser
import system.body.BodyManager
import system.body.BodyParser
import system.DependencyInjector
import system.location.LocationFakeParser
import system.location.LocationManager
import system.location.LocationParser
import kotlin.test.assertEquals

class QuestListenerTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            val bodyParser = BodyFakeParser(listOf(ProtoBody("Human")))
            DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
            BodyManager.reset()

            val locationParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(LocationNode("an open field"))))
            DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
            LocationManager.reset()

            GameState.player = Player(Creature("Player"))
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

        val testEvent = InteractEvent(GameState.player, Item("Apple"))
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

        val testEvent = InteractEvent(GameState.player, Item("Apple"))
        val listener = QuestListener()
        listener.execute(testEvent)
        val results = listener.getListeners()
        assertEquals(0, results.size)
    }
}