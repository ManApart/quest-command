package system

import core.DependencyInjector
import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.body.*
import core.conditional.ConditionalStringPointer
import core.target.Target
import core.target.activator.ActivatorManager
import core.target.activator.dsl.ActivatorsCollection
import core.target.activator.dsl.ActivatorsMock
import core.target.target
import core.utility.NameSearchableList
import org.junit.Before
import org.junit.Test
import system.location.LocationFakeParser
import traveling.position.NO_VECTOR
import traveling.location.location.*
import kotlin.test.assertEquals

class ActivatorManagerTest {

    @Before
    fun setup() {
        val locationParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(
                LocationNode(PLAYER_START_LOCATION, parent = PLAYER_START_NETWORK),
                LocationNode(PLAYER_START_LOCATION, parent = DEFAULT_NETWORK.name))
        ))

        DependencyInjector.setImplementation(NetworksCollection::class.java, NetworksMock())
        DependencyInjector.setImplementation(LocationsCollection::class.java, LocationsMock())
        LocationManager.reset()

        DependencyInjector.setImplementation(BodysCollection::class.java, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class.java, BodyPartsMock())
        BodyManager.reset()
    }

    @Test
    fun topLevelValueIsParameterized() {
        val activator = target("Target"){
            description("This is a \$key")
        }
        val fakeParser = ActivatorsMock(listOf(activator))
        DependencyInjector.setImplementation(ActivatorsCollection::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, NO_VECTOR, mapOf("key" to "value"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("This is a value", result.getDescription())
    }

    @Test
    fun nestedClimbableGetsParams() {
        val activator = target("Target"){
            description("\$destination")
        }
        val fakeParser = ActivatorsMock(listOf(activator))
        DependencyInjector.setImplementation(ActivatorsCollection::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, NO_VECTOR, mapOf("destination" to "resort"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("resort", result.getDescription())
    }

}