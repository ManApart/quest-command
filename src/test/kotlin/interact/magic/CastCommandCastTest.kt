package interact.magic

import core.gameState.Target
import core.gameState.location.LocationNode
import core.utility.reflection.MockReflections
import core.utility.reflection.Reflections
import interact.scope.Scope
import interact.scope.ScopeManager
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.DependencyInjector
import system.EventManager
import system.behavior.BehaviorParser
import system.body.BodyParser
import system.location.LocationFakeParser
import system.location.LocationParser
import kotlin.test.assertTrue

class CastCommandCastTest {

    companion object {

        init {
            DependencyInjector.setImplementation(BehaviorParser::class.java, BehaviorFakeParser())
            DependencyInjector.setImplementation(Reflections::class.java, MockReflections())
            DependencyInjector.setImplementation(LocationParser::class.java, LocationFakeParser())
            DependencyInjector.setImplementation(BodyParser::class.java, BodyFakeParser())
        }

        private val targetA = Target("targetA")
        private val targetB = Target("targetB")
        private val scope = ScopeManager.getScope()

        init {
            scope.addTarget(targetA)
            scope.addTarget(targetB)
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            DependencyInjector.clearImplementation(Reflections::class.java)
            DependencyInjector.clearImplementation(BehaviorParser::class.java)
        }
    }

    @Before
    fun setup() {
        EventManager.clear()
    }

    @Test
    fun castWord() {
        val spellCommand = MockSpellCommand("testSpellA", listOf("catA"))
        val reflections = MockReflections(spellCommands = listOf(spellCommand))
        DependencyInjector.setImplementation(Reflections::class.java, reflections)

        CastCommand().execute("cast", "testspellA".split(" "))

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.isEmpty())
    }

    @Test
    fun castWordWithParams() {
        val spellCommand = MockSpellCommand("testSpellA", listOf("catA"))
        val reflections = MockReflections(spellCommands = listOf(spellCommand))
        DependencyInjector.setImplementation(Reflections::class.java, reflections)

        CastCommand().execute("cast", "testspellA 1 2".split(" "))

        assertTrue(splitStringEqualsList("1 2", spellCommand.args))
        assertTrue(spellCommand.targets.isEmpty())
    }

    @Test
    fun castWordWithTarget() {
        val spellCommand = MockSpellCommand("testSpellA", listOf("catA"))
        val reflections = MockReflections(spellCommands = listOf(spellCommand))
        DependencyInjector.setImplementation(Reflections::class.java, reflections)

        CastCommand().execute("cast", "testspellA on targetA".split(" "))

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.contains(targetA))
    }


    @Test
    fun castWordWithTargetsAndParams() {
        val spellCommand = MockSpellCommand("testSpellA", listOf("catA"))
        val reflections = MockReflections(spellCommands = listOf(spellCommand))
        DependencyInjector.setImplementation(Reflections::class.java, reflections)

        CastCommand().execute("cast", "testspellA 1 2 on targetA and targetB".split(" "))

        assertTrue(splitStringEqualsList("1 2", spellCommand.args))
        assertTrue(spellCommand.targets.contains(targetA))
        assertTrue(spellCommand.targets.contains(targetB))
    }

    private fun splitStringEqualsList(expected: String, actual: List<String>): Boolean {
        return actual.toTypedArray() contentEquals expected.split(" ").toTypedArray()
    }


}