package combat.battle.position

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/*
Direct hit when both axis line up
Grazing hit when one axis lines up
Miss when neither axis lines up
 */
class TargetVectorTest {

    @Test
    fun directHit() {
        val attack = TargetPosition(Horizontal.CENTER, Vertical.CENTER)
        val defender = TargetPosition(Horizontal.CENTER, Vertical.CENTER)

        assertEquals(HitLevel.DIRECT, attack.getHitLevel(defender))
    }

    @Test
    fun grazeHorizontal() {
        val attack = TargetPosition(Horizontal.RIGHT, Vertical.CENTER)
        val defender = TargetPosition(Horizontal.CENTER, Vertical.CENTER)

        assertEquals(HitLevel.GRAZING, attack.getHitLevel(defender))
    }

    @Test
    fun grazeVertical() {
        val attack = TargetPosition(Horizontal.CENTER, Vertical.HIGH)
        val defender = TargetPosition(Horizontal.CENTER, Vertical.CENTER)

        assertEquals(HitLevel.GRAZING, attack.getHitLevel(defender))
    }

    @Test
    fun missDiagonal() {
        val attack = TargetPosition(Horizontal.RIGHT, Vertical.HIGH)
        val defender = TargetPosition(Horizontal.CENTER, Vertical.CENTER)

        assertEquals(HitLevel.MISS, attack.getHitLevel(defender))
    }

    @Test
    fun totalMiss() {
        val attack = TargetPosition(Horizontal.LEFT, Vertical.HIGH)
        val defender = TargetPosition(Horizontal.RIGHT, Vertical.LOW)

        assertEquals(HitLevel.MISS, attack.getHitLevel(defender))
    }

    @Test
    fun equals() {
        val a = TargetPosition(Horizontal.LEFT, Vertical.HIGH)
        val b = TargetPosition(Horizontal.LEFT, Vertical.HIGH)

        val equality = a.equals(b)
        val strictEquality = a == b

        assertTrue(equality)
        assertFalse(strictEquality)
    }
}