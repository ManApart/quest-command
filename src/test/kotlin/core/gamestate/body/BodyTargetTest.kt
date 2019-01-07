package core.gamestate.body

import combat.battle.position.Horizontal
import combat.battle.position.TargetPosition
import combat.battle.position.Vertical
import core.gameState.body.Body
import core.gameState.body.BodyPart
import org.junit.Test
import kotlin.test.assertEquals

class BodyTargetTest {

    @Test
    fun directTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val body = Body(parts = listOf(part))
        val attack = TargetPosition()

        val directParts = body.getDirectParts(attack)

        assertEquals(1, directParts.size)
        assertEquals(part, directParts.first())
    }

    @Test
    fun directTargetAdjusted() {
        val part = BodyPart("Hand", TargetPosition())
        val body = Body(parts = listOf(part))
        val attack = TargetPosition(Horizontal.LEFT, Vertical.HIGH)

        val directParts = body.getDirectParts(attack, TargetPosition(Horizontal.LEFT, Vertical.HIGH))

        assertEquals(1, directParts.size)
        assertEquals(part, directParts.first())
    }

    @Test
    fun grazedTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val body = Body(parts = listOf(part))
        val attack = TargetPosition(Horizontal.LEFT)

        val grazedParts = body.getGrazedParts(attack)

        assertEquals(1, grazedParts.size)
        assertEquals(part, grazedParts.first())
    }

    @Test
    fun grazedTargetAdjusted() {
        val part = BodyPart("Hand", TargetPosition(Horizontal.RIGHT, Vertical.LOW))
        val body = Body(parts = listOf(part))
        val attack = TargetPosition()

        val grazedParts = body.getGrazedParts(attack, TargetPosition(Horizontal.LEFT))

        assertEquals(1, grazedParts.size)
        assertEquals(part, grazedParts.first())
    }

    @Test
    fun missedTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val body = Body(parts = listOf(part))
        val attack = TargetPosition(Horizontal.LEFT, Vertical.HIGH)

        val directParts = body.getDirectParts(attack)
        val grazedParts = body.getGrazedParts(attack)

        assertEquals(0, directParts.size)
        assertEquals(0, grazedParts.size)
    }

}