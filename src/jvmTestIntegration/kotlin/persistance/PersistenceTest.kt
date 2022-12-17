package persistance

import core.GameManager
import core.GameState
import core.ai.behavior.BehaviorRecipe
import core.ai.knowledge.Fact
import core.ai.knowledge.Subject
import core.body.Body
import core.commands.CommandParsers
import core.events.EventManager
import core.properties.PropertiesP
import core.properties.props
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import status.conditions.Condition
import status.conditions.ConditionP
import status.effects.Effect
import status.effects.EffectBase
import status.effects.EffectP
import status.stat.LeveledStat
import status.stat.LeveledStatP
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
        EventManager.reset()
        GameManager.newGame(playerName = "Saved Player", testing = true)
        EventManager.executeEvents()
        File("./savesTest/").listFiles()?.forEach { it.deleteRecursively() }
    }

    @After
    fun deleteSaves() {
        File("./savesTest/").listFiles()?.forEach { it.deleteRecursively() }
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
            tag("one", "Two")
            value("health", 100)
        }
        //retain casing
        original.values.put("check", "Upper")
        val json = Json.encodeToString(PropertiesP(original))
        val parsed: PropertiesP = Json.decodeFromString(json)
        val actual = parsed.parsed()
        assertEquals(original, actual)
        assertEquals(original.values.getString("check"), actual.values.getString("check"))
    }

    @Test
    fun leveledStat() {
        val original = LeveledStat("health", 1, 1, 2)
        val json = Json.encodeToString(LeveledStatP(original))
        val parsed: LeveledStatP = Json.decodeFromString(json)
        val actual = parsed.parsed()
        with(actual) {
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
        val json = Json.encodeToString(EffectP(original))
        val parsed: EffectP = Json.decodeFromString(json)
        assertEffectMatches(original, parsed.parsed(body))
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

        val json = Json.encodeToString(ConditionP(original))
        val parsed: ConditionP = Json.decodeFromString(json)

        with(parsed) {
            assertEquals(original.name, name)
            assertEquals(original.element, element)
            assertEquals(original.elementStrength, elementStrength)
            assertEquals(original.permanent, permanent)
            assertEquals(original.age, age)
            assertEquals(original.isCritical, isCritical)
            assertEquals(original.isFirstApply, isFirstApply)
        }
        assertEffectMatches(original.effects.first(), parsed.effects.first().parsed(body))
    }

    @Test
    fun playerSave() {
        val preLoadPlayer = GameState.getPlayer("Saved Player")!!
        CommandParsers.parseCommand(preLoadPlayer, "rs 1 && move to wheat && slash wheat && pickup wheat && ne")
        EventManager.executeEvents()

        preLoadPlayer.thing.properties.tags.add("Saved")
        //subject: thingIsSelf
        //subject: locationIsName
        val fact = Fact(Subject(preLoadPlayer.thing), "Neat")
        preLoadPlayer.mind.learn(fact)

        EventManager.postEvent(SaveEvent(preLoadPlayer))
        EventManager.executeEvents()
        preLoadPlayer.thing.properties.tags.remove("Saved")
        assertFalse(preLoadPlayer.thing.properties.tags.has("Saved"))
        val equippedItemCount = preLoadPlayer.thing.body.getEquippedItems().size

        EventManager.postEvent(LoadEvent(preLoadPlayer, "Kanbara"))
        EventManager.executeEvents()
        val postLoadPlayer = GameState.getPlayer("Saved Player")!!
        assertEquals("Saved Player", postLoadPlayer.thing.name)
        assertTrue(postLoadPlayer.thing.properties.tags.has("Saved"))
        assertEquals(equippedItemCount, postLoadPlayer.thing.body.getEquippedItems().size)
        assertEquals(fact, postLoadPlayer.mind.memory.getFact(fact.source, fact.kind))

        CommandParsers.parseCommand(postLoadPlayer, "travel to open field && r")
        val location = postLoadPlayer.thing.location.getLocation()
        val bundles = location.getItems("bundle")
        assertTrue(bundles.isNotEmpty(), "Should have found wheat bundles")
        assertEquals(2, bundles.first().properties.getCount())
    }

}