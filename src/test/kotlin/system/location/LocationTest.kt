package system.location

import core.properties.Properties
import core.properties.Values
import core.target.Target
import org.junit.Test
import traveling.location.location.Location
import traveling.location.location.LocationNode
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocationTest {
    @Test
    fun hasRoomForItem(){
        val item = createItem("Apple", weight = 2)
        val location = Location(LocationNode("Loc"), properties = Properties(Values(mapOf("Size" to "3"))))
        assertTrue(location.hasRoomFor(item))
    }

    @Test
    fun doesNotHaveRoomForItem(){
        val item = createItem("Apple", weight = 5)
        val location = Location(LocationNode("Loc"), properties = Properties(Values(mapOf("Size" to "3"))))
        assertFalse(location.hasRoomFor(item))
    }

    private fun createItem(name: String, weight: Int) : Target {
        return Target(name, properties = Properties(Values(mapOf("weight" to weight.toString()))))
    }

}