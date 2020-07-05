import core.events.Event
import core.events.EventListener
import core.utility.reflection.MockReflections
import core.reflection.Reflections
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import core.DependencyInjector
import core.events.EventManager

class EventManagerTest {
    private val resultList = mutableListOf<String>()
    private val first = TestListener(this, 0, "first")
    private val second = TestListener(this, 1, "second")
    private val child = TestChildListener(this, 2, "child")

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

    companion object {
        @BeforeClass
        @JvmStatic fun setupAll() {
            DependencyInjector.setImplementation(Reflections::class.java, MockReflections())
        }

        @AfterClass
        @JvmStatic fun teardown() {
            DependencyInjector.clearImplementation(Reflections::class.java)
        }
    }


    @Test
    fun higherPriorityRunsSooner(){
        EventManager.registerListener(first)
        EventManager.registerListener(second)

        EventManager.postEvent(TestEvent())
        EventManager.executeEvents()

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
        EventManager.executeEvents()

        EventManager.unRegisterListener(first)
        EventManager.unRegisterListener(second)

        Assert.assertEquals("first", resultList[0])
        Assert.assertEquals("second", resultList[1])
        resultList.clear()
    }

    @Test
    fun childClassStillFound(){
        EventManager.registerListener(child)

        EventManager.postEvent(TestEvent())
        EventManager.executeEvents()

        EventManager.unRegisterListener(child)

        Assert.assertEquals("child", resultList[0])
        resultList.clear()
    }

}