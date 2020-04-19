package core.body

import core.target.Target
import org.junit.Test
import traveling.location.location.LocationRecipe
import kotlin.test.assertEquals

class BodyEquipTest {


    @Test
    fun equipItem() {
        val item = Target("Dagger", equipSlots = listOf(listOf("Grip")))
        val part = LocationRecipe("Hand", slots = listOf("Grip", "Glove"))
        val body = createBody(part)

        body.equip(item)

        assertEquals(1, body.getEquippedItems().size)
        assertEquals(1, body.getEquippedItemsAt("Grip").size)
        assertEquals(item, body.getEquippedItemsAt("Grip").first())
    }

    @Test
    fun unEquipItem() {
        val item = Target("Dagger", equipSlots = listOf(listOf("Grip")))
        val part = LocationRecipe("Hand", slots = listOf("Grip", "Glove"))
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
        val rightHand = LocationRecipe("Right Hand", slots = listOf("Right Grip", "Right Glove"))
        val leftHand = LocationRecipe("Left Hand", slots = listOf("Left Grip", "Left Glove"))
        val body = createBody(listOf(rightHand, leftHand))

        body.equip(dagger)
        body.equip(hatchet)

        assertEquals(2, body.getEquippedItems().size)
        assertEquals(1, body.getEquippedItemsAt("Right Grip").size)
        assertEquals(1, body.getEquippedItemsAt("Left Grip").size)
    }

    @Test
    fun equipPrefersRightSide() {
        val dagger = Target("Dagger", equipSlots = listOf(listOf("Left Grip"), listOf("Right Grip")))
        val rightHand = LocationRecipe("Right Hand", slots = listOf("Right Grip", "Right Glove"))
        val leftHand = LocationRecipe("Left Hand", slots = listOf("Left Grip", "Left Glove"))
        val body = createBody(listOf(leftHand, rightHand))

        body.equip(dagger)

        assertEquals(1, body.getEquippedItemsAt("Right Grip").size)
    }

    @Test
    fun replaceEquippedItem() {
        val dagger = Target("Dagger", equipSlots = listOf(listOf("Right Grip"), listOf("Left Grip")))
        val hatchet = Target("Hatchet", equipSlots = listOf(listOf("Right Grip"), listOf("Left Grip")))
        val rightHand = LocationRecipe("Right Hand", slots = listOf("Right Grip", "Right Glove"))
        val leftHand = LocationRecipe("Left Hand", slots = listOf("Left Grip", "Left Glove"))
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
        val rightFoot = LocationRecipe("Right Foot", slots = listOf("Right Foot"))
        val rightLeg = LocationRecipe("Right Leg", slots = listOf("Right Leg"))
        val body = createBody(listOf(rightFoot, rightLeg))

        body.equip(boot, boot.equipSlots.first())
        body.equip(shoe, shoe.equipSlots.first())

        assertEquals(1, body.getEquippedItems().size)
        assertEquals(0, body.getEquippedItemsAt("Right Leg").size)
        assertEquals(1, body.getEquippedItemsAt("Right Foot").size)
        assertEquals(shoe, body.getEquippedItemsAt("Right Foot").first())
    }
}