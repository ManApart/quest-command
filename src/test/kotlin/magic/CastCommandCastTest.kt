package magic

import traveling.position.TargetAim
import core.target.Target
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import system.BodyFakeParser
import core.DependencyInjector
import core.GameState
import core.ai.behavior.BehaviorsCollection
import core.ai.behavior.BehaviorsMock
import core.events.EventManager
import injectAllDefaultMocks
import magic.spellCommands.SpellCommandsMock
import magic.castSpell.CastCommand
import magic.castSpell.getTargetedPartsOrAll
import magic.spellCommands.SpellCommandsCollection
import traveling.location.location.Location
import traveling.location.location.LocationNode
import traveling.location.location.LocationParser
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CastCommandCastTest {

    companion object {
        init {
            injectAllDefaultMocks()
        }

        private val targetA = Target("targetA")
        private val targetB = Target("targetB")
        private val scope = GameState.currentLocation()

        init {
            scope.addTarget(targetA)
            scope.addTarget(targetB)
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            DependencyInjector.clearImplementation(SpellCommandsCollection::class.java)
            DependencyInjector.clearImplementation(BehaviorsCollection::class.java)
            DependencyInjector.clearImplementation(LocationParser::class.java)
        }
    }

    @Before
    fun setup() {
        EventManager.clear()
    }

    @Test
    fun castWord() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class.java, reflections)

        CastCommand().execute("cast", "testspellA".split(" "))

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.isEmpty())
    }

    @Test
    fun castWordWithParams() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class.java, reflections)

        CastCommand().execute("cast", "testspellA 1 2".split(" "))

        assertEquals("1 2", spellCommand.args.fullString)
        assertTrue(spellCommand.targets.isEmpty())
    }

    @Test
    fun castWordWithTarget() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class.java, reflections)

        CastCommand().execute("cast", "testspellA on targetA".split(" "))

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(targetsContainByName(spellCommand.targets, targetA))
    }

    @Test
    fun castWordWithTargetsAndParams() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class.java, reflections)

        CastCommand().execute("cast", "testspellA 1 2 on targetA and targetB".split(" "))

        assertEquals("1 2", spellCommand.args.fullString)
        assertTrue(targetsContainByName(spellCommand.targets, targetA))
        assertTrue(targetsContainByName(spellCommand.targets, targetB))
    }

    private fun targetsContainByName(targetAims: List<TargetAim>, target: Target): Boolean {
        return targetAims.map { it.target }.firstOrNull { target.name == it.name } != null
    }

    @Test
    fun limitParts() {
        val part = Location(LocationNode("leg"))
        val target = TargetAim(Target("Bob"), listOf(part))

        val results = getTargetedPartsOrAll(target, 3)

        assertEquals(1, results.size)
        assertEquals(part, results.first())
    }


}
