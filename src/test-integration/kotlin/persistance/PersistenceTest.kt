package persistance

import core.GameManager
import core.GameState
import core.PRINT_WITHOUT_FLUSH
import core.ai.behavior.BehaviorRecipe
import core.body.Body
import core.commands.CommandParsers
import core.events.EventManager
import core.properties.Properties
import core.properties.props
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import status.conditions.Condition
import status.effects.Effect
import status.effects.EffectBase
import status.stat.LeveledStat
import system.persistance.loading.LoadEvent
import system.persistance.saving.SaveEvent
import traveling.location.Network
import traveling.location.location.Location
import traveling.location.location.locationRecipe
import traveling.location.network.LocationNode
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
    fun leveledStat() {
        val original = LeveledStat("health", 1, 1, 2)
        val json = Json.encodeToString(original)
        val parsed: LeveledStat = Json.decodeFromString(json)
        with(parsed) {
            assertEquals(original.name, name)
            assertEquals(original.level, level)
            assertEquals(original.getMaxMultiplier(), getMaxMultiplier())
            assertEquals(original.expExponential, expExponential)
            assertEquals(original.max, max)
            assertEquals(original.current, current)
            assertEquals(original.xp, xp)
        }
    }

    @Test
    fun effect() {
        val locationRecipe = locationRecipe("Head") { }.build()
        val location = Location(LocationNode("Head"), recipe = locationRecipe)
        val original = Effect(EffectBase("Base", "thingy"), 1, 2, listOf(location))
        val body = Body("Head", Network("Head", locationRecipe))
        val json = Json.encodeToString(original)
        val parsed: Effect = Json.decodeFromString(json)
        parsed.afterLoad(body)
        assertEffectMatches(original, parsed)
    }

    private fun assertEffectMatches(original: Effect, parsed: Effect) {
        with(parsed) {
            assertEquals(original.name, name)
            assertEquals(original.amount, amount)
            assertEquals(original.duration, duration)
            assertEquals(1, bodyPartTargets.size)
        }
    }

    @Test
    fun condition() {
        val locationRecipe = locationRecipe("Head") { }.build()
        val location = Location(LocationNode("Head"), recipe = locationRecipe)
        val effect = Effect(EffectBase("Base", "thingy"), 1, 2, listOf(location))
        val original = Condition("Fever", effects = listOf(effect))
        val body = Body("Head", Network("Head", locationRecipe))

        val json = Json.encodeToString(original)
        val parsed: Condition = Json.decodeFromString(json)
        parsed.afterLoad(body)

        with(parsed) {
            assertEquals(original.name, name)
            assertEquals(original.element, element)
            assertEquals(original.elementStrength, elementStrength)
            assertEquals(original.permanent, permanent)
            assertEquals(original.age, age)
            assertEquals(original.isCritical, isCritical)
            assertEquals(original.isFirstApply, isFirstApply)
        }
        assertEffectMatches(original.effects.first(), parsed.effects.first())
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