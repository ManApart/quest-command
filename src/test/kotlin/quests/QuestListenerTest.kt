package quests

import core.DependencyInjector
import core.GameManager
import core.GameState
import core.body.*
import core.target.Target
import org.junit.BeforeClass
import org.junit.Test

import traveling.location.location.*
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock

import use.interaction.InteractEvent
import kotlin.test.assertEquals

class QuestListenerTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
            DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
            BodyManager.reset()

            DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
            DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
            LocationManager.reset()

            GameState.player = GameManager.newPlayer()
        }
    }

    @Test
    fun oneQuestListenerIsRemovedOnExecute() {
        val event1 = StoryEvent("Test Quest", 10, "journal", ConditionalEvents(InteractEvent::class, { event, _ -> event.target.name == "Pie" }))
        val event2 = StoryEvent("Test Quest2", 10, "journal", ConditionalEvents(InteractEvent::class, { event, _ -> event.target.name == "Apple" }))

        val questList = StoryEventsMock(listOf(event1, event2))
        DependencyInjector.setImplementation(StoryEventsCollection::class, questList)
        QuestManager.reset()

        //Only the apple event is executed
        val testEvent = InteractEvent(GameState.player, Target("Apple"))
        val listener = QuestListener()
        listener.execute(testEvent)

        val results = listener.getListeners()
        assertEquals(1, results.size)
        assertEquals(1, results[InteractEvent::class]?.size)
    }

    @Test
    fun bothQuestListenersAreRemovedOnExecute() {
        val event1 = StoryEvent("Test Quest", 10, "journal", ConditionalEvents(InteractEvent::class))
        val event2 = StoryEvent("Test Quest2", 10, "journal", ConditionalEvents(InteractEvent::class))

        val questList = StoryEventsMock(listOf(event1, event2))
        DependencyInjector.setImplementation(StoryEventsCollection::class, questList)
        QuestManager.reset()

        //Both events are executed
        val testEvent = InteractEvent(GameState.player, Target("Apple"))
        val listener = QuestListener()

        listener.execute(testEvent)

        val results = listener.getListeners()
        assertEquals(0, results.size)
        assertEquals(null, results[InteractEvent::class])
    }

    @Test
    fun eventListIsRemovedIfEmpty() {
        val event1 = StoryEvent("Test Quest", 10, "journal", ConditionalEvents(InteractEvent::class))

        val questList = StoryEventsMock(listOf(event1))
        DependencyInjector.setImplementation(StoryEventsCollection::class, questList)
        QuestManager.reset()

        val testEvent = InteractEvent(GameState.player, Target("Apple"))
        val listener = QuestListener()
        listener.execute(testEvent)
        val results = listener.getListeners()
        assertEquals(0, results.size)
    }
}