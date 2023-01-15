package inventory

import core.DependencyInjector
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.body.*
import core.thing.Thing
import inventory.dropItem.PlaceItem
import inventory.dropItem.PlaceItemEvent
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.network.NOWHERE_NODE
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import kotlin.test.BeforeTest
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlaceItemTest {

    @BeforeTest
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

        runBlocking { NOWHERE_NODE.getLocation().clear() }
    }

    @Test
    fun dropItem() {
        val creature = Thing("Creature")
        val item = Thing("Apple")
        creature.inventory.add(item)
        val scope = runBlocking { creature.location.getLocation() }

        runBlocking { PlaceItem().execute(PlaceItemEvent(creature, item)) }
        assertTrue(scope.getThings(item.name).isNotEmpty())
        assertNull(creature.inventory.getItem(item.name))
    }

}