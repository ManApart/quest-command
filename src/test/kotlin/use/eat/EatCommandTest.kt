package use.eat

import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorParser
import core.body.BodyManager
import core.commands.CommandParser
import core.events.EventManager
import core.properties.Properties
import core.properties.Tags
import core.reflection.Reflections
import core.target.Target
import core.target.item.ITEM_TAG
import core.utility.PoorMansInstrumenter
import core.utility.reflection.MockReflections
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser
import traveling.location.location.NOWHERE_NODE
import use.UseEvent
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
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        GameState.player = GameManager.newPlayer(location = NOWHERE_NODE)
    }

    @Test
    fun eatFood() {
        val timer = PoorMansInstrumenter(10000)
        val item = Target("Pear", properties = Properties(tags = Tags(listOf("Food", ITEM_TAG))))
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
        val fruit = Target("Pear", properties = Properties(tags = Tags(listOf("Food", ITEM_TAG))))
        val pie = Target("Pear Pie", properties = Properties(tags = Tags(listOf("Food", ITEM_TAG))))
        GameState.player.inventory.add(fruit)
        GameState.player.inventory.add(pie)
        EatCommand().execute("eat", listOf("Pear"))
        val events = EventManager.getUnexecutedEvents()

        assertEquals(0, events.size)
        assertNotNull(CommandParser.getResponseRequest())
    }
}