package core.events

import core.DependencyInjector
import createMockedGame
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

import kotlin.test.assertEquals

class EventManagerTest {
    private val resultList = mutableListOf<String>()
    private val first = TestListener(this, 0, "first")
    private val second = TestListener(this, 1, "second")
    private val child = TestChildListener(this, 2, "child")
    private val eventListenerMap = EventListenerMapMock(mapOf(
        "TestEvent" to listOf(first, second, child)
    ))

    class TestEvent : Event
    open class TestListener(private val parent: EventManagerTest, private val priorityLevel: Int, private val id: String) : EventListener<TestEvent>() {
        override fun getPriorityRank(): Int {
            return priorityLevel
        }

        override fun execute(event: TestEvent) {
            parent.resultList.add(id)
        }

    }

    class TestChildListener(parent: EventManagerTest, priorityLevel: Int, id: String) : TestListener(parent, priorityLevel, id)

    @BeforeTest
    fun setup(){
        DependencyInjector.setImplementation(EventListenerMapCollection::class, eventListenerMap)
        EventManager.reset()
    }

    @AfterTest
    fun teardown(){
        EventManager.clear()
        resultList.clear()
    }

    @Test
    fun higherPriorityRunsSooner(){
        EventManager.postEvent(TestEvent())
        EventManager.executeEvents()

        assertEquals("first", resultList[0])
        assertEquals("second", resultList[1])
        assertEquals("child", resultList[2])
    }

}
