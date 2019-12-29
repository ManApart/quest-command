package magic.spellCommands.water

import combat.DamageType
import combat.battle.position.TargetAim
import core.DependencyInjector
import core.ai.behavior.BehaviorParser
import core.body.BodyParser
import core.commands.Args
import core.events.EventManager
import core.reflection.Reflections
import core.target.Target
import core.utility.reflection.MockReflections
import magic.MockSpellCommand
import magic.castSpell.StartCastSpellEvent
import magic.spells.Spell
import org.junit.AfterClass
import org.junit.Before
import org.junit.Test
import status.effects.EffectBase
import status.effects.EffectFakeParser
import status.effects.EffectParser
import status.stat.FOCUS
import status.stat.StatEffect
import status.stat.WATER_MAGIC
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.LocationParser
import traveling.scope.ScopeManager
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
            DependencyInjector.setImplementation(EffectParser::class.java, EffectFakeParser(listOf(
                    EffectBase("Heal", "", "Health", statEffect = StatEffect.RECOVER, damageType = DamageType.WATER),
                    EffectBase("Wet", "", statTarget = "Agility", statEffect = StatEffect.DEPLETE, damageType = DamageType.WATER)
            )))
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
        assertEquals(5, spell.condition.elementStrength)
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
        Heal().execute(targetA, args, listOf(TargetAim(targetA)), true)
        return (EventManager.getUnexecutedEvents().firstOrNull() as StartCastSpellEvent).spell
    }


}