package system

import core.DependencyInjector
import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.body.*
import core.thing.activator.ActivatorManager
import core.thing.activator.dsl.ActivatorsCollection
import core.thing.activator.dsl.ActivatorsMock
import core.thing.thing
import org.junit.Before
import org.junit.Test
import traveling.location.location.*
import traveling.location.network.DEFAULT_NETWORK
import traveling.location.network.NetworksCollection
import traveling.location.network.NetworksMock
import traveling.location.network.networks
import traveling.position.NO_VECTOR
import kotlin.test.assertEquals

class ActivatorManagerTest {

    @Before
    fun setup() {
        val networksMock = NetworksMock(networks {
            network(DEFAULT_NETWORK.name){
                locationNode(PLAYER_START_LOCATION)
            }
            network(PLAYER_START_NETWORK){
                locationNode(PLAYER_START_LOCATION)
            }
        })

        DependencyInjector.setImplementation(NetworksCollection::class, networksMock)
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        DependencyInjector.setImplementation(BodysCollection::class, BodysMock())
        DependencyInjector.setImplementation(BodyPartsCollection::class, BodyPartsMock())
        BodyManager.reset()
    }

    @Test
    fun topLevelValueIsParameterized() {
        val activator = thing("Thing"){
            description("This is a \$key")
        }
        val mock = ActivatorsMock(listOf(activator))
        DependencyInjector.setImplementation(ActivatorsCollection::class, mock)
        ActivatorManager.reset()

        val thing = LocationThing("Thing", null, NO_VECTOR, mapOf("key" to "value"))
        val result = ActivatorManager.getActivatorsFromLocationThings(listOf(thing)).first()

        assertEquals("This is a value", result.description)
    }

    @Test
    fun nestedClimbableGetsParams() {
        val activator = thing("Thing"){
            description("\$destination")
        }
        val mock = ActivatorsMock(listOf(activator))
        DependencyInjector.setImplementation(ActivatorsCollection::class, mock)
        ActivatorManager.reset()

        val thing = LocationThing("Thing", null, NO_VECTOR, mapOf("destination" to "resort"))
        val result = ActivatorManager.getActivatorsFromLocationThings(listOf(thing)).first()

        assertEquals("resort", result.description)
    }

}