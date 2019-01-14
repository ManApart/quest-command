package system

import core.gameState.Activator
import core.gameState.climb.Climbable
import core.gameState.location.LocationTarget
import core.utility.NameSearchableList
import dialogue.DialogueOptions
import org.junit.BeforeClass
import org.junit.Test
import system.activator.ActivatorManager
import system.activator.ActivatorParser
import system.location.LocationFakeParser
import system.location.LocationManager
import system.location.LocationParser
import kotlin.test.assertEquals

class ActivatorManagerTest {

    companion object {
        @JvmStatic @BeforeClass
        fun setup() {
            //Run before other tests so object is initialized and we're testing a fresh clear each time
            val fakeParser = ActivatorFakeParser(NameSearchableList(listOf()))
            DependencyInjector.setImplementation(ActivatorParser::class.java, fakeParser)
            ActivatorManager.getAll()

            val locationParser = LocationFakeParser(locationNodes = NameSearchableList(listOf()))
            DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
            LocationManager.getLocations()
        }
    }

    @Test
    fun topLevelValueIsParameterized() {
        val activator = Activator("Target", DialogueOptions("This is a \$key"))
        val fakeParser = ActivatorFakeParser(NameSearchableList(listOf(activator)))
        DependencyInjector.setImplementation(ActivatorParser::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, mapOf("key" to "value"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("This is a value", result.description)
    }

    @Test
    fun nestedClimbableGetsParams() {
        val activator = Activator("Target", climb = Climbable("\$destination", "", true))
        val fakeParser = ActivatorFakeParser(NameSearchableList(listOf(activator)))
        DependencyInjector.setImplementation(ActivatorParser::class.java, fakeParser)
        ActivatorManager.reset()

        val target = LocationTarget("Target", null, mapOf("destination" to "resort"))
        val result = ActivatorManager.getActivatorsFromLocationTargets(listOf(target)).first()

        assertEquals("resort", result.climb?.name ?: "")
    }

}