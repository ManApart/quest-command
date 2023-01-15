package explore.look

import core.GameState
import core.thing.Thing
import createMockedGame
import kotlinx.coroutines.runBlocking
import system.debug.DebugType
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LookCommandTest {

    @BeforeTest
    fun setup() {
        createMockedGame()
        runBlocking { GameState.player.location.getLocation().addThing(Thing("Bob")) }
    }

    @Test
    fun blankSuggest() {
        runBlocking {
            val args = listOf<String>()
            val expected = listOf("all", "body", "hand", "Player")
            val actual = LookCommand().suggest(GameState.player, "look", args)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun ofBody() {
        runBlocking {
            val args = listOf("body")
            val expected = listOf("of")
            val actual = LookCommand().suggest(GameState.player, "look", args)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun ofHand() {
        runBlocking {
            val args = listOf("hand")
            val expected = listOf("of")
            val actual = LookCommand().suggest(GameState.player, "look", args)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun ofThing() {
        runBlocking {
            val args = listOf("hand", "of")
            val expected = listOf("Player", "Bob")
            GameState.putDebug(DebugType.CLARITY, true)
            val actual = LookCommand().suggest(GameState.player, "look", args)
            assertEquals(expected, actual)
            GameState.putDebug(DebugType.CLARITY, false)
        }
    }
}