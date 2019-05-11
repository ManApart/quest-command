package core.gamestate.body

import combat.Combatant
import combat.attack.AttackedPartHelper
import combat.battle.position.Horizontal
import combat.battle.position.TargetPosition
import combat.battle.position.Vertical
import core.gameState.Target
import core.gameState.body.BodyPart
import org.junit.Test
import system.BodyFakeParser
import system.DependencyInjector
import system.body.BodyManager
import system.body.BodyParser
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AttackedPartHelperTest {

    @Test
    fun directTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val helper = createHelper(part, TargetPosition())
        val foundPart = helper.getAttackedPart()

        assertEquals(part.name, foundPart?.name)
    }

    @Test
    fun directTargetAdjusted() {
        val part = BodyPart("Hand", TargetPosition())
        val target = TargetPosition(Horizontal.LEFT, Vertical.HIGH)
        val adjustment = TargetPosition(Horizontal.LEFT, Vertical.HIGH)
        val helper = createHelper(part, target, adjustment)
        val foundPart = helper.getAttackedPart()

        assertEquals(part.name, foundPart?.name)
    }

    @Test
    fun grazedTarget() {
        val part = BodyPart("Hand", TargetPosition())
        val target = TargetPosition(Horizontal.LEFT)

        val helper = createHelper(part, target)
        val foundPart = helper.getAttackedPart()

        assertEquals(part.name, foundPart?.name)
    }

    @Test
    fun grazedTargetAdjusted() {
        val part = BodyPart("Hand", TargetPosition(Horizontal.RIGHT, Vertical.LOW))
        val target = TargetPosition()
        val adjustment = TargetPosition(Horizontal.LEFT)

        val helper = createHelper(part, target, adjustment)
        val foundPart = helper.getAttackedPart()

        assertEquals(part.name, foundPart?.name)
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
        val directPart = BodyPart("directPart", TargetPosition())
        val grazedPart = BodyPart("grazedPart", TargetPosition(Horizontal.LEFT))

        val bodyParser = BodyFakeParser.parserFromParts(listOf(directPart, grazedPart))
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val combatant = Combatant(Target(name = "Target", body = "body"))

        val helper = AttackedPartHelper(combatant, TargetPosition())

        val foundPart = helper.getAttackedPart()

        assertEquals(directPart.name, foundPart?.name)
    }

    @Test
    fun blockedPartReturnedBeforeDirectPart() {
        val blockPosition = TargetPosition(Horizontal.LEFT)

        val directPart = BodyPart("directPart", TargetPosition())
        val grazedPart = BodyPart("grazedPart", blockPosition)

        val bodyParser = BodyFakeParser.parserFromParts(listOf(directPart, grazedPart))
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val combatant = Combatant(Target(name = "Target", body = "body"))
        combatant.blockBodyPart = grazedPart
        combatant.blockPosition = blockPosition

        val helper = AttackedPartHelper(combatant, TargetPosition())

        val foundPart = helper.getAttackedPart()

        assertEquals(grazedPart, foundPart)
    }

    @Test
    fun blockedPartNotReturnedWhenBlockPositionMissed() {
        val blockPosition = TargetPosition(Horizontal.LEFT, Vertical.HIGH)

        val directPart = BodyPart("directPart", TargetPosition())
        val grazedPart = BodyPart("grazedPart", blockPosition)

        val bodyParser = BodyFakeParser.parserFromParts(listOf(directPart, grazedPart))
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val combatant = Combatant(Target(name = "Target", body = "body"))
        combatant.blockBodyPart = grazedPart
        combatant.blockPosition = blockPosition

        val helper = AttackedPartHelper(combatant, TargetPosition())

        val foundPart = helper.getAttackedPart()

        assertEquals(directPart.name, foundPart?.name)
    }

    private fun createHelper(part: BodyPart, target: TargetPosition, combatantPosition: TargetPosition = TargetPosition()): AttackedPartHelper {

        val bodyParser = BodyFakeParser.parserFromPart(part)
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val combatant = Combatant(Target(name = "Target", body = "body"))
        combatant.position = combatantPosition
        return AttackedPartHelper(combatant, target)
    }

}