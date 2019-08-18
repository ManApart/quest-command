package interact.magic

import core.utility.reflection.MockReflections
import core.utility.reflection.Reflections
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import system.DependencyInjector
import system.EventManager
import kotlin.test.*

class CastCommandWordTest {

    companion object {
        private val spellA = MockSpellCommand("testSpellA", listOf("catA"))
        private val spellB = MockSpellCommand("testSpellB", listOf("catA"))
        private val spellC = MockSpellCommand("testSpellC")

        @BeforeClass
        @JvmStatic
        fun setupAll() {
            val reflections = MockReflections(spellCommands = listOf(
                    spellA, spellB, spellC
            ))
            DependencyInjector.setImplementation(Reflections::class.java, reflections)
        }


        @AfterClass
        @JvmStatic
        fun teardown() {
            DependencyInjector.clearImplementation(Reflections::class.java)
        }
    }

    @Before
    fun setup() {
        EventManager.clear()
    }

    @Test
    fun listWords() {
        CastCommand().execute("word", listOf("list"))

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNull(event.word)
        assertFalse(event.groups)
    }

    @Test
    fun listWordsWithoutArgs() {
        CastCommand().execute("word", listOf())

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNull(event.word)
        assertFalse(event.groups)
    }

    @Test
    fun listWordsInCategory() {
        CastCommand().execute("word", listOf("catA"))

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNotNull(event.word)
        assertTrue(event.groups)
    }

    @Test
    fun helpSpecificWord() {
        CastCommand().execute("word", listOf("spellB"))

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertTrue(events[0] is ViewWordHelpEvent)
        val event = events[0] as ViewWordHelpEvent
        assertNotNull(event.word)
        assertFalse(event.groups)
    }

}