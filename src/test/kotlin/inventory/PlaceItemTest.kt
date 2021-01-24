package inventory

import core.DependencyInjector
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.BodyManager
import core.target.Target
import inventory.dropItem.PlaceItem
import inventory.dropItem.PlaceItemEvent
import org.junit.Before
import org.junit.Test
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.location.*
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlaceItemTest {

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorsMock()
        DependencyInjector.setImplementation(BehaviorsCollection::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        NOWHERE_NODE.getLocation().clear()
    }

    @Test
    fun dropItem() {
        val creature = Target("Creature")
        val item = Target("Apple")
        creature.inventory.add(item)
        val scope = creature.location.getLocation()

        PlaceItem().execute(PlaceItemEvent(creature, item))
        assertTrue(scope.getTargets(item.name).isNotEmpty())
        assertNull(creature.inventory.getItem(item.name))
    }

}