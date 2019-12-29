package persistance

import core.GameManager
import core.GameState
import core.events.EventManager
import org.junit.After
import org.junit.Before
import org.junit.Test
import system.persistance.loading.LoadEvent
import system.persistance.saving.SaveEvent
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
    fun testThing() {
        GameState.player.properties.tags.add("Saved")
        EventManager.postEvent(SaveEvent())
        EventManager.executeEvents()
        GameState.player.properties.tags.remove("Saved")
        assertFalse(GameState.player.properties.tags.has("Saved"))
        EventManager.postEvent(LoadEvent())
        EventManager.executeEvents()
        assertTrue(GameState.player.properties.tags.has("Saved"))
    }

}