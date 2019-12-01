package interact.eat

import core.commands.CommandParser
import core.gameState.*
import core.gameState.Target
import core.gameState.location.NOWHERE_NODE
import core.utility.PoorMansInstrumenter
import core.utility.reflection.MockReflections
import core.utility.reflection.Reflections
import interact.UseEvent
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.DependencyInjector
import system.EventManager
import system.behavior.BehaviorManager
import system.behavior.BehaviorParser
import system.body.BodyManager
import system.body.BodyParser
import system.location.LocationFakeParser
import system.location.LocationManager
import system.location.LocationParser
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EatCommandTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setupAll() {
            DependencyInjector.setImplementation(Reflections::class.java, MockReflections())
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            DependencyInjector.clearImplementation(Reflections::class.java)
            DependencyInjector.clearImplementation(BehaviorParser::class.java)
            DependencyInjector.clearImplementation(LocationParser::class.java)
        }
    }

    @Before
    fun setup() {
        EventManager.clear()
        CommandParser.setResponseRequest(null)

        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        GameState.player = Player(location = NOWHERE_NODE)
    }

    @Test
    fun eatFood() {
        val timer = PoorMansInstrumenter(10000)
        val item = Target("Pear", properties = Properties(tags = Tags(listOf("Food"))))
        timer.printElapsed("new item")
        GameState.player.inventory.add(item)
        timer.printElapsed("add item")
        EatCommand().execute("eat", listOf("Pear"))
        timer.printElapsed("execute event")
        val events = EventManager.getUnexecutedEvents()
        timer.printElapsed("get events")

        assertEquals(1, events.size)
        assertTrue(events[0] is UseEvent)
        assertEquals(item, (events[0] as UseEvent).used)
        assertNull(CommandParser.getResponseRequest())
    }

    @Test
    fun eatMultipleFoodGivesChoice() {
        val fruit = Target("Pear", properties = Properties(tags = Tags(listOf("Food"))))
        val pie = Target("Pear Pie", properties = Properties(tags = Tags(listOf("Food"))))
        GameState.player.inventory.add(fruit)
        GameState.player.inventory.add(pie)
        EatCommand().execute("eat", listOf("Pear"))
        val events = EventManager.getUnexecutedEvents()

        assertEquals(0, events.size)
        assertNotNull(CommandParser.getResponseRequest())
    }
}