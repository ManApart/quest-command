package explore.look

import core.GameState
import core.thing.Thing
import createMockedGame
import system.debug.DebugType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LookCommandTest {

    @BeforeTest
    fun setup(){
        createMockedGame()
        GameState.player.location.getLocation().addThing(Thing("Bob"))
    }

    @Test
    fun blankSuggest(){
        val args = listOf<String>()
        val expected = listOf("all", "body", "hand", "Player")
        val actual = LookCommand().suggest(GameState.player, "look", args)
        assertEquals(expected, actual)
    }

    @Test
    fun ofBody(){
        val args = listOf("body")
        val expected = listOf("of")
        val actual = LookCommand().suggest(GameState.player, "look", args)
        assertEquals(expected, actual)
    }

    @Test
    fun ofHand(){
        val args = listOf("hand")
        val expected = listOf("of")
        val actual = LookCommand().suggest(GameState.player, "look", args)
        assertEquals(expected, actual)
    }

    @Test
    fun ofThing(){
        val args = listOf("hand", "of")
        val expected = listOf("Player", "Bob")
        GameState.putDebug(DebugType.CLARITY, true)
        val actual = LookCommand().suggest(GameState.player, "look", args)
        assertEquals(expected, actual)
        GameState.putDebug(DebugType.CLARITY, false)
    }
}