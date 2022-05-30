package magic

import core.DependencyInjector
import core.GameManager
import core.events.EventManager
import createMockedGame
import magic.castSpell.CastCommand
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsMock
import org.junit.jupiter.api.BeforeAll

import kotlin.test.*

class CastCommandWordTest {

    companion object {
        private val spellA = SpellCommandMock("testSpellA", listOf("catA"))
        private val spellB = SpellCommandMock("testSpellB", listOf("catA"))
        private val spellC = SpellCommandMock("testSpellC")
        private val player = GameManager.newPlayer()

        @BeforeAll
        @JvmStatic
        fun setupAll() {
            createMockedGame()
            val reflections = SpellCommandsMock(listOf(
                    spellA, spellB, spellC
            ))
            DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)
        }

    }

    @BeforeTest
    fun setup() {
        EventManager.clear()
    }

    @Test
    fun listWords() {
        CastCommand().execute(player,"word", listOf("list"))

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNull(event.word)
        assertFalse(event.groups)
    }

    @Test
    fun listWordsWithoutArgs() {
        CastCommand().execute(player,"word", listOf())

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNull(event.word)
        assertFalse(event.groups)
    }

    @Test
    fun listWordsInCategory() {
        CastCommand().execute(player,"word", listOf("catA"))

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNotNull(event.word)
        assertTrue(event.groups)
    }

    @Test
    fun helpSpecificWord() {
        CastCommand().execute(player,"word", listOf("spellB"))

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNotNull(event.word)
        assertFalse(event.groups)
    }

}
