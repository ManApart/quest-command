package magic.spellCommands.water

import combat.DamageType
import core.DependencyInjector
import core.GameState
import core.Player
import core.commands.Args
import core.events.EventManager
import core.thing.Thing
import createMockedGame
import magic.SpellCommandMock
import magic.castSpell.StartCastSpellEvent
import magic.spellCommands.SpellCommandsCollection
import magic.spellCommands.SpellCommandsMock
import magic.spells.Spell
import org.junit.Before
import org.junit.Test
import status.effects.EffectBase
import status.effects.EffectManager
import status.effects.EffectsCollection
import status.effects.EffectsMock
import status.stat.FOCUS
import status.stat.StatEffect
import status.stat.WATER_MAGIC
import traveling.position.ThingAim
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HealTest {
    companion object {
        init {
            createMockedGame()

            DependencyInjector.setImplementation(EffectsCollection::class, EffectsMock(listOf(
                    EffectBase("Heal", "", "Health", statEffect = StatEffect.RECOVER, damageType = DamageType.WATER),
                    EffectBase("Wet", "", statThing = "Agility", statEffect = StatEffect.DEPLETE, damageType = DamageType.WATER)
            )))
            EffectManager.reset()
        }

        private val thingA = Player("Thinga", Thing("thingA"))
        private val scope = GameState.player.thing.currentLocation()

        init {
            scope.addThing(thingA.thing)
        }

    }

    @Before
    fun setup() {
        EventManager.clear()
        thingA.soul.setStat(WATER_MAGIC, 20)
        thingA.soul.addStat(FOCUS, 10, 100, 1)
    }

    @Test
    fun defaultArgs() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        val spell = castHeal("")

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.things.isEmpty())

        assertNotNull(spell)
        assertEquals(5, spell.condition.elementStrength)
        assertEquals(1, spell.condition.getEffects()[0].duration)
    }

    @Test
    fun amountAndDuration() {
        val spellCommand = SpellCommandMock("testSpellA", listOf("catA"))
        val reflections = SpellCommandsMock(listOf(spellCommand))
        DependencyInjector.setImplementation(SpellCommandsCollection::class, reflections)

        val spell = castHeal("5 for 10")

        assertTrue(spellCommand.args.isEmpty())
        assertTrue(spellCommand.things.isEmpty())

        assertNotNull(spell)
        assertEquals(5, spell.condition.elementStrength)
        assertEquals(10, spell.condition.getEffects()[0].duration)
    }


    private fun castHeal(input: String): Spell {
        val args = Args(input.split(" "), delimiters = listOf("on"))
        Heal().execute(thingA, args, listOf(ThingAim(thingA.thing)), true)
        return (EventManager.getUnexecutedEvents().firstOrNull() as StartCastSpellEvent).spell
    }


}
