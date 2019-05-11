package system

import core.gameState.PLAYER_START_LOCATION
import core.gameState.PLAYER_START_NETWORK
import core.gameState.Target
import core.gameState.climb.Climbable
import core.gameState.location.DEFAULT_NETWORK
import core.gameState.location.LocationNode
import core.gameState.location.LocationTarget
import core.utility.NameSearchableList
import dialogue.DialogueOptions
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.activator.ActivatorManager
import system.activator.ActivatorParser
import system.body.BodyManager
import system.body.BodyParser
import system.location.LocationFakeParser
import system.location.LocationManager
import system.location.LocationParser
import kotlin.test.assertEquals

class ActivatorManagerTest {

    @Before
    fun setup() {
        val locationParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(
                LocationNode(PLAYER_START_LOCATION, parent = PLAYER_START_NETWORK),
                LocationNode(PLAYER_START_LOCATION, parent = DEFAULT_NETWORK))
        ))

        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()
    }

    @Test
    fun topLevelValueIsParameterized() {
        val activator = Target("Target", dynamicDescription = DialogueOptions("This is a \$key"))
        val fakeParser = ActivatorFakeParser(NameSearchableList(listOf(activator)))
        DependencyInjector.setImplementation(ActivatorParser::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, mapOf("key" to "value"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("This is a value", result.description)
    }

    @Test
    fun nestedClimbableGetsParams() {
        val activator = Target("Target", climb = Climbable("\$destination", "", true))
        val fakeParser = ActivatorFakeParser(NameSearchableList(listOf(activator)))
        DependencyInjector.setImplementation(ActivatorParser::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, mapOf("destination" to "resort"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("resort", result.climb?.name ?: "")
    }

}