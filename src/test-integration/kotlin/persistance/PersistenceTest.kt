package persistance

import core.GameManager
import core.GameState
import core.PRINT_WITHOUT_FLUSH
import core.commands.CommandParser
import core.events.EventManager
import org.junit.After
import org.junit.Before
import org.junit.Test
import system.persistance.loading.LoadEvent
import system.persistance.saving.SaveEvent
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PersistenceTest {
    @Before
    fun reset() {
        EventManager.clear()
        EventManager.registerListeners()
        GameManager.newGame(playerName = "Saved Player")
        GameState.properties.values.put(PRINT_WITHOUT_FLUSH, true)

        EventManager.executeEvents()
        File("./saves/").listFiles()?.forEach { it.deleteRecursively() }
    }

    @After
    fun deleteSaves() {
        File("./saves/").listFiles()?.forEach { it.deleteRecursively() }
    }

    @Test
    fun playerSave() {
        CommandParser.parseCommand("move to wheat && slash wheat && pickup wheat && ne")
        EventManager.executeEvents()
        GameState.player.target.properties.tags.add("Saved")

        EventManager.postEvent(SaveEvent(GameState.player))
        EventManager.executeEvents()
        GameState.player.target.properties.tags.remove("Saved")
        assertFalse(GameState.player.target.properties.tags.has("Saved"))
        val equippedItemCount = GameState.player.target.body.getEquippedItems().size

        EventManager.postEvent(LoadEvent(GameState.player, "Kanbara"))
        EventManager.executeEvents()
        assertEquals("Saved Player", GameState.player.target.name)
        assertTrue(GameState.player.target.properties.tags.has("Saved"))
        assertEquals(equippedItemCount, GameState.player.target.body.getEquippedItems().size)

        CommandParser.parseCommand("travel to open field && r")
        assertEquals(2, GameState.player.target.location.getLocation().getItems("bundle").first().properties.getCount())
    }

}