package inventory

import core.DependencyInjector
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.*
import core.target.Target
import inventory.dropItem.PlaceItem
import inventory.dropItem.PlaceItemEvent
import org.junit.Before
import org.junit.Test
import traveling.location.location.*
import traveling.location.network.NOWHERE_NODE
import traveling.location.network.NetworksCollection
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlaceItemTest {

    @Before
    fun setup() {
        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()

        val behaviorParser = BehaviorsMock()
        DependencyInjector.setImplementation(BehaviorsCollection::class, behaviorParser)
        BehaviorManager.reset()

        DependencyInjector.setImplementation(NetworksCollection::class, NetworksMock())
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
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