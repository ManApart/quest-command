package core.commands

import core.GameState
import core.events.EventManager
import createMockedGame
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ThingFinderTest{

    @Before
    fun setup() {
        createMockedGame()
        GameState.player.thing.currentLocation().addThing(GameState.player.thing)
        EventManager.clear()
    }

    @Test
    fun simpleCase(){
        val command = "player:hand"

        val result = findThing(GameState.player.thing, command)
        assertNull(result?.thing)
        assertNotNull(result?.location)
        assertEquals("right hand", result?.location?.name)
    }

    @Test
    fun moreSpecific(){
        val command = "player:left hand"

        val result = findThing(GameState.player.thing, command)
        assertNull(result?.thing)
        assertNotNull(result?.location)
        assertEquals("left hand", result?.location?.name)
    }

}