package explore.look

import core.GameState
import core.thing.Thing
import createMockedGame
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
        val expected = listOf("all", "body", "hand", "Bob")
        val args = listOf<String>()
        val actual = LookCommand().suggest(GameState.player, "look", args)
        assertEquals(expected, actual)
    }
}