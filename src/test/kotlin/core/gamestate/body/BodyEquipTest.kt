package core.gamestate.body

import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.body.Slot
import org.junit.Test
import kotlin.test.assertEquals

class BodyEquipTest {


    @Test
    fun equipItem() {
        val item = Target("Dagger", equipSlots = listOf(listOf("Grip")))
        val part = BodyPart("Hand",  listOf("Grip", "Glove"))
        val body = createBody(part)

        body.equip(item)

        assertEquals(1, body.getEquippedItems().size)
        assertEquals(1, body.getEquippedItemsAt("Grip").size)
        assertEquals(item, body.getEquippedItemsAt("Grip").first())
    }

    @Test
    fun unEquipItem() {
        val item = Target("Dagger", equipSlots = listOf(listOf("Grip")))
        val part = BodyPart("Hand",  listOf("Grip", "Glove"))
        val body = createBody(part)

        body.equip(item)
        body.unEquip(item)

        assertEquals(0, body.getEquippedItems().size)
        assertEquals(0, body.getEquippedItemsAt("Grip").size)
    }

    @Test
    fun equipItemToFreeSlot() {
        val dagger = Target("Dagger", equipSlots = listOf(listOf("Right Grip"), listOf("Left Grip")))
        val hatchet = Target("Hatchet", equipSlots = listOf(listOf("Right Grip"), listOf("Left Grip")))
        val rightHand = BodyPart("Right Hand",  listOf("Right Grip", "Right Glove"))
        val leftHand = BodyPart("Left Hand",  listOf("Left Grip", "Left Glove"))
        val body = createBody(listOf(rightHand, leftHand))

        body.equip(dagger)
        body.equip(hatchet)

        assertEquals(2, body.getEquippedItems().size)
        assertEquals(1, body.getEquippedItemsAt("Right Grip").size)
        assertEquals(1, body.getEquippedItemsAt("Left Grip").size)
    }

    @Test
    fun replaceEquippedItem() {
        val dagger = Target("Dagger", equipSlots = listOf(listOf("Right Grip"), listOf("Left Grip")))
        val hatchet = Target("Hatchet", equipSlots = listOf(listOf("Right Grip"), listOf("Left Grip")))
        val rightHand = BodyPart("Right Hand",  listOf("Right Grip", "Right Glove"))
        val leftHand = BodyPart("Left Hand",  listOf("Left Grip", "Left Glove"))
        val body = createBody(listOf(rightHand, leftHand))
        val slot = Slot(listOf("Right Grip"))

        body.equip(dagger, slot)
        body.equip(hatchet, slot)

        assertEquals(1, body.getEquippedItems().size)
        assertEquals(1, body.getEquippedItemsAt("Right Grip").size)
        assertEquals(0, body.getEquippedItemsAt("Left Grip").size)
    }

    @Test
    fun replaceOverlappedEquippedItem() {
        val shoe = Target("Shoe", equipSlots = listOf(listOf("Right Foot")))
        val boot = Target("Boot", equipSlots = listOf(listOf("Right Foot", "Right Leg")))
        val rightFoot= BodyPart("Right Foot",  listOf("Right Foot"))
        val rightLeg = BodyPart("Right Leg",  listOf("Right Leg"))
        val body = createBody(listOf(rightFoot, rightLeg))

        body.equip(boot, boot.equipSlots.first())
        body.equip(shoe, shoe.equipSlots.first())

        assertEquals(1, body.getEquippedItems().size)
        assertEquals(0, body.getEquippedItemsAt("Right Leg").size)
        assertEquals(1, body.getEquippedItemsAt("Right Foot").size)
        assertEquals(shoe, body.getEquippedItemsAt("Right Foot").first())
    }
}