package system.location

import core.GameState
import core.properties.Properties
import core.properties.Values
import core.thing.Thing
import core.utility.toNameSearchableList
import createMockedGame
import kotlin.test.Test


import traveling.location.location.Location
import traveling.location.network.LocationNode
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocationTest {
    @BeforeTest
    fun setup(){
        createMockedGame()
    }

    @Test
    fun hasRoomForItem(){
        val item = createItem("Apple", weight = 2)
        val location = Location(LocationNode("Loc"), properties = Properties(Values("Size" to "3")))
        assertTrue(location.hasRoomFor(item))
    }

    @Test
    fun doesNotHaveRoomForItem(){
        val item = createItem("Apple", weight = 5)
        val location = Location(LocationNode("Loc"), properties = Properties(Values("Size" to "3")))
        assertFalse(location.hasRoomFor(item))
    }

    @Test
    fun getAllSouls(){
        val items = listOf(createItem("Apple", 5),
        createItem("Dagger", 5),
        createItem("Sword", 5))
        val location = Location(LocationNode("Loc"), items = items.toNameSearchableList(), properties = Properties(Values("Size" to "3")))
        val souls = location.getAllSouls(GameState.player.thing)
        assertEquals(4, souls.size)
        assertTrue(souls.contains(GameState.player.thing.soul))
    }

    private fun createItem(name: String, weight: Int) : Thing {
        return Thing(name, properties = Properties(Values("weight" to weight.toString())))
    }

}