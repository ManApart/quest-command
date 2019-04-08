package core.gamestate.body

import combat.Combatant
import combat.attack.AttackedPartHelper
import combat.battle.position.Horizontal
import combat.battle.position.TargetPosition
import combat.battle.position.Vertical
import core.gameState.Creature
import core.gameState.body.Body
import core.gameState.body.BodyPart
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AttackedPartHelperTest {

    @Test
    fun directTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val helper = createHelper(part, TargetPosition())
        val foundPart = helper.getAttackedPart()

        assertEquals(part, foundPart)
    }

    @Test
    fun directTargetAdjusted() {
        val part = BodyPart("Hand", TargetPosition())
        val target = TargetPosition(Horizontal.LEFT, Vertical.HIGH)
        val adjustment = TargetPosition(Horizontal.LEFT, Vertical.HIGH)
        val helper = createHelper(part, target, adjustment)
        val foundPart = helper.getAttackedPart()

        assertEquals(part, foundPart)
    }

    @Test
    fun grazedTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val target = TargetPosition(Horizontal.LEFT)

        val helper = createHelper(part, target)
        val foundPart = helper.getAttackedPart()

        assertEquals(part, foundPart)
    }

    @Test
    fun grazedTargetAdjusted() {
        val part = BodyPart("Hand", TargetPosition(Horizontal.RIGHT, Vertical.LOW))
        val target = TargetPosition()
        val adjustment = TargetPosition(Horizontal.LEFT)

        val helper = createHelper(part, target, adjustment)
        val foundPart = helper.getAttackedPart()

        assertEquals(part, foundPart)
    }

    @Test
    fun missedTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val target = TargetPosition(Horizontal.LEFT, Vertical.HIGH)

        val helper = createHelper(part, target)
        val foundPart = helper.getAttackedPart()

        assertNull(foundPart)
    }

    @Test
    fun directPartReturnedBeforeGrazedPart() {
        val directPart = BodyPart("Hand", TargetPosition())
        val grazedPart = BodyPart("Hand", TargetPosition(Horizontal.LEFT))
        val body = Body(parts = listOf(directPart, grazedPart))
        val combatant = Combatant(Creature(name = "Creature", body = body))

        val helper = AttackedPartHelper(combatant, TargetPosition())

        val foundPart = helper.getAttackedPart()

        assertEquals(directPart, foundPart)
    }

    @Test
    fun blockedPartReturnedBeforeDirectPart() {
        val blockPosition = TargetPosition(Horizontal.LEFT)

        val directPart = BodyPart("Hand", TargetPosition())
        val grazedPart = BodyPart("Hand", blockPosition)
        val body = Body(parts = listOf(directPart, grazedPart))

        val combatant = Combatant(Creature(name = "Creature", body = body))
        combatant.blockBodyPart = grazedPart
        combatant.blockPosition = blockPosition

        val helper = AttackedPartHelper(combatant, TargetPosition())

        val foundPart = helper.getAttackedPart()

        assertEquals(grazedPart, foundPart)
    }

    @Test
    fun blockedPartNotReturnedWhenBlockPositionMissed() {
        val blockPosition = TargetPosition(Horizontal.LEFT, Vertical.HIGH)

        val directPart = BodyPart("Hand", TargetPosition())
        val grazedPart = BodyPart("Hand", blockPosition)
        val body = Body(parts = listOf(directPart, grazedPart))

        val combatant = Combatant(Creature(name = "Creature", body = body))
        combatant.blockBodyPart = grazedPart
        combatant.blockPosition = blockPosition

        val helper = AttackedPartHelper(combatant, TargetPosition())

        val foundPart = helper.getAttackedPart()

        assertEquals(directPart, foundPart)
    }

    private fun createHelper(part: BodyPart, target: TargetPosition, combatantPosition: TargetPosition = TargetPosition()): AttackedPartHelper {
        val body = Body(parts = listOf(part))
        val combatant = Combatant(Creature(name = "Creature", body = body))
        combatant.position = combatantPosition
        return AttackedPartHelper(combatant, target)
    }

}