package interact.magic.spellCommands.water

import combat.battle.position.TargetAim
import core.commands.Args
import core.gameState.GameState
import core.gameState.Target
import core.gameState.stat.FOCUS
import core.gameState.stat.WATER_MAGIC
import core.utility.reflection.MockReflections
import core.utility.reflection.Reflections
import interact.magic.CastCommand
import interact.magic.MockSpellCommand
import interact.magic.StartCastSpellEvent
import interact.magic.spells.Spell
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HealTest {
    companion object {
        init {
            DependencyInjector.setImplementation(BehaviorParser::class.java, BehaviorFakeParser())
            DependencyInjector.setImplementation(Reflections::class.java, MockReflections())
            DependencyInjector.setImplementation(LocationParser::class.java, LocationFakeParser())
            DependencyInjector.setImplementation(BodyParser::class.java, BodyFakeParser())
            EventManager.reset()
        }

        private val targetA = Target("targetA")
        private val scope = ScopeManager.getScope()

        init {
            scope.addTarget(targetA)
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            DependencyInjector.clearImplementation(Reflections::class.java)
            DependencyInjector.clearImplementation(BehaviorParser::class.java)
            DependencyInjector.clearImplementation(LocationParser::class.java)
            DependencyInjector.clearImplementation(BodyParser::class.java)
            EventManager.clear()
        }
    }

    @Before
    fun setup() {
        EventManager.clear()
        targetA.soul.setStat(WATER_MAGIC, 20)
        targetA.soul.addStat(FOCUS, 10, 100, 1)
    }

    @Test
    fun defaultArgs() {
        val spellCommand = MockSpellCommand("testSpellA", listOf("catA"))
        val reflections = MockReflections(spellCommands = listOf(spellCommand))
        DependencyInjector.setImplementation(Reflections::class.java, reflections)

        val spell = castHeal("")

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.isEmpty())

        assertNotNull(spell)
        assertEquals(1, spell.condition.elementStrength)
        assertEquals(1, spell.condition.getEffects()[0].duration)
    }

    @Test
    fun amountAndDuration() {
        val spellCommand = MockSpellCommand("testSpellA", listOf("catA"))
        val reflections = MockReflections(spellCommands = listOf(spellCommand))
        DependencyInjector.setImplementation(Reflections::class.java, reflections)

        val spell = castHeal("5 for 10")

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.targets.isEmpty())

        assertNotNull(spell)
        assertEquals(5, spell.condition.elementStrength)
        assertEquals(10, spell.condition.getEffects()[0].duration)
    }


    private fun castHeal(input: String): Spell? {
        val args = Args(input.split(" "), delimiters = listOf("on"))
        Heal().execute(targetA, args, listOf(TargetAim(targetA)))
        return (EventManager.getUnexecutedEvents().firstOrNull() as StartCastSpellEvent).spell
    }


}