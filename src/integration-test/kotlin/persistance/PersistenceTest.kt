package persistance

import core.GameManager
import core.GameState
import core.events.EventManager
import org.junit.After
import org.junit.Before
import org.junit.Test
import system.persistance.loading.LoadEvent
import system.persistance.saving.SaveEvent
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PersistenceTest {
    @Before
    fun reset() {
        EventManager.clear()
        EventManager.registerListeners()
        GameManager.newGame()
        EventManager.executeEvents()
    }

    @After
    fun deleteSaves() {
//        File("./saves/").listFiles()?.forEach { it.delete() }
    }

    @Test
    fun playerSave() {
        GameState.player.properties.tags.add("Saved")
        GameState.player.givenName = "Saved Player"

        EventManager.postEvent(SaveEvent())
        EventManager.executeEvents()
        GameState.player.properties.tags.remove("Saved")
        assertFalse(GameState.player.properties.tags.has("Saved"))

        EventManager.postEvent(LoadEvent("SavedPlayer"))
        EventManager.executeEvents()
        assertEquals("Saved Player", GameState.player.givenName)
        assertTrue(GameState.player.properties.tags.has("Saved"))
    }

}