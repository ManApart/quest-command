import events.Event
import events.EventListener
import org.junit.Assert
import org.junit.Test
import processing.EventManager

class EventManagerTest {
    private val resultList = mutableListOf<String>()
    private val first = TestListener(this, 0, "first")
    private val second = TestListener(this, 1, "second")

    class TestEvent : Event
    class TestListener(private val parent: EventManagerTest, private val priorityLevel: Int, private val id: String) : EventListener<TestEvent>() {
        override fun getPriority(): Int {
            return priorityLevel
        }

        override fun handle(event: TestEvent) {
            parent.resultList.add(id)
        }

    }

    @Test
    fun higherPriorityRunsSooner(){
        EventManager.registerListener(first)
        EventManager.registerListener(second)

        EventManager.postEvent(TestEvent())

        EventManager.unRegisterListener(first)
        EventManager.unRegisterListener(second)

        Assert.assertEquals("first", resultList[0])
        Assert.assertEquals("second", resultList[1])
        resultList.clear()
    }

    @Test
    fun higherPriorityRunsSoonerRegardlessOfAddOrder(){
        EventManager.registerListener(second)
        EventManager.registerListener(first)

        EventManager.postEvent(TestEvent())

        EventManager.unRegisterListener(first)
        EventManager.unRegisterListener(second)

        Assert.assertEquals("first", resultList[0])
        Assert.assertEquals("second", resultList[1])
        resultList.clear()
    }
}