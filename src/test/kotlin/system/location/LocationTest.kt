package system.location

import core.GameState
import core.properties.Properties
import core.properties.Values
import core.target.Target
import core.utility.toNameSearchableList
import createMockedGame
import org.junit.Before
import org.junit.Test
import traveling.location.location.Location
import traveling.location.network.LocationNode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocationTest {
    @Before
    fun setup(){
        createMockedGame()
    }

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

    @Test
    fun getAllSouls(){
        val items = listOf(createItem("Apple", 5),
        createItem("Dagger", 5),
        createItem("Sword", 5))
        val location = Location(LocationNode("Loc"), items = items.toNameSearchableList(), properties = Properties(Values(mapOf("Size" to "3"))))
        val souls = location.getAllSouls()
        assertEquals(4, souls.size)
        assertTrue(souls.contains(GameState.player.soul))
    }

    private fun createItem(name: String, weight: Int) : Target {
        return Target(name, properties = Properties(Values(mapOf("weight" to weight.toString()))))
    }

}