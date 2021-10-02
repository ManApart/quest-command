package magic

import core.DependencyInjector
import core.GameManager
import core.GameState
import core.events.EventManager
import core.target.Target
import createMockedGame
import magic.castSpell.CastCommand
import magic.castSpell.getTargetedPartsOrAll
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsMock
import org.junit.Before
import org.junit.Test
import traveling.location.location.Location
import traveling.location.network.LocationNode
import traveling.position.TargetAim
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CastCommandCastTest {

    companion object {
        init {
            createMockedGame()
        }

        private val targetA = Target("targetA")
        private val targetB = Target("targetB")
        private val scope = GameState.player.currentLocation()
        private val player = GameManager.newPlayer()

        init {
            scope.addTarget(targetA)
            scope.addTarget(targetB)
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
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        CastCommand().execute(player,"cast", "testspellA".split(" "))

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.isEmpty())
    }

    @Test
    fun castWordWithParams() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        CastCommand().execute(player,"cast", "testspellA 1 2".split(" "))

        assertEquals("1 2", spellCommand.args.fullString)
        assertTrue(spellCommand.targets.isEmpty())
    }

    @Test
    fun castWordWithTarget() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        CastCommand().execute(player,"cast", "testspellA on targetA".split(" "))

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(targetsContainByName(spellCommand.targets, targetA))
    }

    @Test
    fun castWordWithTargetsAndParams() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        CastCommand().execute(player,"cast", "testspellA 1 2 on targetA and targetB".split(" "))

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
