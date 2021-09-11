package system

import core.DependencyInjector
import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.body.*
import core.target.activator.ActivatorManager
import core.target.activator.dsl.ActivatorsCollection
import core.target.activator.dsl.ActivatorsMock
import core.target.target
import org.junit.Before
import org.junit.Test
import traveling.location.location.*
import traveling.location.network.DEFAULT_NETWORK
import traveling.location.network.NetworksCollection
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

        DependencyInjector.setImplementation(NetworksCollection::class.java, networksMock)
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
        val mock = ActivatorsMock(listOf(activator))
        DependencyInjector.setImplementation(ActivatorsCollection::class.java, mock)
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
        val mock = ActivatorsMock(listOf(activator))
        DependencyInjector.setImplementation(ActivatorsCollection::class.java, mock)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, NO_VECTOR, mapOf("destination" to "resort"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("resort", result.getDescription())
    }

}