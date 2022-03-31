package persistance

import core.GameManager
import core.GameState
import core.PRINT_WITHOUT_FLUSH
import core.ai.behavior.BehaviorRecipe
import core.commands.CommandParsers
import core.events.EventManager
import core.properties.Properties
import core.properties.PropsBuilder
import core.properties.props
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    fun behaviorRecipe() {
        val original = BehaviorRecipe("thing", mapOf("this" to "that"))
        val json = Json.encodeToString(original)
        val parsed: BehaviorRecipe = Json.decodeFromString(json)
        assertEquals(original, parsed)
    }

    @Test
    fun properties() {
        val original = props {
            tag("one", "two")
            value("health", 100)
        }
        val json = Json.encodeToString(original)
        val parsed: Properties = Json.decodeFromString(json)
        assertEquals(original, parsed)
    }

    @Test
    fun playerSave() {
        CommandParsers.parseCommand(GameState.player, "move to wheat && slash wheat && pickup wheat && ne")
        EventManager.executeEvents()
        GameState.player.thing.properties.tags.add("Saved")

        EventManager.postEvent(SaveEvent(GameState.player))
        EventManager.executeEvents()
        GameState.player.thing.properties.tags.remove("Saved")
        assertFalse(GameState.player.thing.properties.tags.has("Saved"))
        val equippedItemCount = GameState.player.thing.body.getEquippedItems().size

        EventManager.postEvent(LoadEvent(GameState.player, "Kanbara"))
        EventManager.executeEvents()
        assertEquals("Saved Player", GameState.player.thing.name)
        assertTrue(GameState.player.thing.properties.tags.has("Saved"))
        assertEquals(equippedItemCount, GameState.player.thing.body.getEquippedItems().size)

        CommandParsers.parseCommand(GameState.player, "travel to open field && r")
        val location = GameState.player.thing.location.getLocation()
        val bundles = location.getItems("bundle")
        assertTrue(bundles.isNotEmpty(), "Should have found wheat bundles")
        assertEquals(2, bundles.first().properties.getCount())
    }

}