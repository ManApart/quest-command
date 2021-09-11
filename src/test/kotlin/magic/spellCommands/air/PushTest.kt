package magic.spellCommands.air

import combat.DamageType
import traveling.position.TargetAim
import core.DependencyInjector
import core.GameState
import core.commands.Args
import core.events.EventManager
import core.properties.WEIGHT
import core.target.Target
import createMockedGame
import magic.castSpell.StartCastSpellEvent
import magic.spells.MoveTargetSpell
import org.junit.Before
import org.junit.Test
import status.effects.EffectBase
import status.effects.EffectManager
import status.effects.EffectsCollection
import status.effects.EffectsMock
import status.stat.AIR_MAGIC
import status.stat.FOCUS
import status.stat.StatEffect
import traveling.position.NO_VECTOR
import traveling.position.Vector
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PushTest {

    companion object {
        init {
            createMockedGame()

            DependencyInjector.setImplementation(EffectsCollection::class.java, EffectsMock(listOf(
                    EffectBase("Air Blasted", "", "Health", statEffect = StatEffect.RECOVER, damageType = DamageType.AIR)
            )))
            EffectManager.reset()
        }

        private val caster = Target("caster")
        private val victim = Target("victim")
        private val scope = GameState.currentLocation()

        init {
            scope.addTarget(caster)
            scope.addTarget(victim)
        }
    }

    @Before
    fun setup() {
        EventManager.clear()
        caster.position = NO_VECTOR
        victim.position = NO_VECTOR
        caster.soul.setStat(AIR_MAGIC, 100)
        caster.soul.addStat(FOCUS, 10, 100, 1)
    }

    @Test
    fun pushFromSamePosition() {
        val spell = castSpell("cast push 20 on victim")

        assertEquals(Vector(0, 20, 0), spell.vector)
    }

    @Test
    fun pushIsDependantOnPowerAndWeight() {
        victim.properties.values.put(WEIGHT, 2)
        val spell = castSpell("cast push 20 on victim")
        victim.properties.values.clear(WEIGHT)

        assertEquals(Vector(0, 10, 0), spell.vector)
    }

    @Test
    fun pushFromDifferentPosition() {
        victim.position = Vector(y = 15)
        val spell = castSpell("cast push 5 on victim")

        assertEquals(Vector(0, 20, 0), spell.vector)
    }

    @Test
    fun pushInDirectionFromSamePosition() {
        val spell = castSpell("cast push 10 towards west on victim")

        assertEquals(Vector(-10, 0, 0), spell.vector)
    }

    @Test
    fun pushInDifferentDirectionFromSamePosition() {
        val spell = castSpell("cast push 10 towards south on victim")

        assertEquals(Vector(0, -10, 0), spell.vector)
    }

    @Test
    fun pushInDirectionFromDifferentPosition() {
        victim.position = Vector(y = 15)
        val spell = castSpell("cast push 10 towards east on victim")

        assertEquals(Vector(10, 15, 0), spell.vector)
    }

    //Allow pushing to an exact location?
//    @Test
//    fun pushTowardsVector() {
//        val spell = castSpell("cast push towards (20, 0, 0) on victim")
//
//        assertEquals(Vector(20, 0, 0), spell.vector)
//    }

    private fun castSpell(input: String): MoveTargetSpell {
        val args = Args(input.split(" "), delimiters = listOf("on"))
        Push().execute(caster, args, listOf(TargetAim(victim)), false)
        val spell = (EventManager.getUnexecutedEvents().firstOrNull() as StartCastSpellEvent).spell as MoveTargetSpell?
        assertNotNull(spell)
        return spell
    }

}
