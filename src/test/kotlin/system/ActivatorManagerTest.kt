package system

import core.DependencyInjector
import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import core.body.BodyManager
import core.conditional.ConditionalStringPointer
import core.target.Target
import core.target.activator.ActivatorManager
import core.target.activator.ActivatorParser
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

        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()
    }

    @Test
    fun topLevelValueIsParameterized() {
        val activator = Target("Target", dynamicDescription = ConditionalStringPointer("This is a \$key"))
        val fakeParser = ActivatorFakeParser(NameSearchableList(listOf(activator)))
        DependencyInjector.setImplementation(ActivatorParser::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, NO_VECTOR, mapOf("key" to "value"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("This is a value", result.getDescription())
    }

    @Test
    fun nestedClimbableGetsParams() {
//        val activator = Target("Target", climb = Climbable("\$destination", "", true))
        val activator = Target("Target", dynamicDescription = ConditionalStringPointer("\$destination"))
        val fakeParser = ActivatorFakeParser(NameSearchableList(listOf(activator)))
        DependencyInjector.setImplementation(ActivatorParser::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, NO_VECTOR, mapOf("destination" to "resort"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("resort", result.getDescription())
    }

}