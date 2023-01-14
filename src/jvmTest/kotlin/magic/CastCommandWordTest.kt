package magic

import core.DependencyInjector
import core.GameManager
import core.GameState.player
import core.events.EventManager
import createMockedGame
import kotlinx.coroutines.runBlocking
import magic.castSpell.CastCommand
import magic.castSpell.WordCommand
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsMock
import org.junit.jupiter.api.BeforeAll

import kotlin.test.*

class CastCommandWordTest {

    companion object {
        private val spellA = SpellCommandMock("testSpellA", listOf("catA"))
        private val spellB = SpellCommandMock("testSpellB", listOf("catA"))
        private val spellC = SpellCommandMock("testSpellC")

        @BeforeAll
        @JvmStatic
        fun setupAll() {
            createMockedGame()
            val reflections = SpellCommandsMock(
                listOf(
                    spellA, spellB, spellC
                )
            )
            DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)
        }

    }

    @BeforeTest
    fun setup() {
        EventManager.clear()
    }

    @Test
    fun listWords() {
        runBlocking { WordCommand().execute(player, "word", listOf("list")) }

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNull(event.word)
        assertFalse(event.groups)
    }

    @Test
    fun listWordsWithoutArgs() {
        runBlocking { WordCommand().execute(player, "word", listOf()) }

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNull(event.word)
        assertFalse(event.groups)
    }

    @Test
    fun listWordsInCategory() {
        runBlocking { WordCommand().execute(player, "word", listOf("catA")) }

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNotNull(event.word)
        assertTrue(event.groups)
    }

    @Test
    fun helpSpecificWord() {
        runBlocking { WordCommand().execute(player, "word", listOf("spellB")) }

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNotNull(event.word)
        assertFalse(event.groups)
    }

}
