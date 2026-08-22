package core.body

import core.body.BodyPartStrings.LEFT_HAND
import core.body.BodyPartStrings.RIGHT_FOOT
import core.body.BodyPartStrings.RIGHT_HAND
import core.body.BodyPartStrings.RIGHT_LEG
import core.body.EquipLayerStrings.CLOTHING
import core.body.EquipLayerStrings.GRIP
import kotlin.test.Test
import core.thing.thing
import kotlinx.coroutines.runBlocking


import traveling.location.location.LocationRecipe
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BodyEquipTest {


    @Test
    fun equipItem() {
        runBlocking {
            val item = thing("Dagger") {
                equipTo(GRIP, "Hand")
            }.build()
            val body = body("Test", "Hand")

            body.equip(item)

            assertEquals(1, body.getEquipped().size)
            assertEquals(1, body.getEquippedAt(GRIP).size)
            assertEquals(item, body.getEquippedAt(GRIP).first())
        }
    }

    @Test
    fun unEquipItem() {
        runBlocking {
            val item = thing("Dagger") {
                equipTo(GRIP, "Hand")
            }.build()
            val body = body("Test", "Hand")

            body.equip(item)
            body.unEquip(item)

            assertEquals(0, body.getEquipped().size)
            assertEquals(0, body.getEquippedAt(GRIP).size)
        }
    }

    @Test
    fun equipItemToFreeSlot() {
        runBlocking {
            val dagger = thing("Dagger") {
                equipTo(GRIP, RIGHT_HAND)
                equipTo(GRIP, LEFT_HAND)
            }.build()
            val hatchet = thing("Hatchet") {
                equipTo(GRIP, RIGHT_HAND)
                equipTo(GRIP, LEFT_HAND)
            }.build()

            val body = body("Test", RIGHT_HAND, LEFT_HAND)

            body.equip(dagger)
            body.equip(hatchet)

            assertEquals(2, body.getEquipped().size)
            assertNotNull(body.getEquippedAt(RIGHT_HAND, GRIP))
            assertNotNull(body.getEquippedAt(LEFT_HAND, GRIP))
        }
    }

    @Test
    fun equipPrefersRightSide() {
        runBlocking {
            val dagger = thing("Dagger") {
                equipTo(GRIP, RIGHT_HAND)
                equipTo(GRIP, LEFT_HAND)
            }.build()

            val body = body("Test", RIGHT_HAND, LEFT_HAND)

            body.equip(dagger)

            assertNotNull(body.getEquippedAt(RIGHT_HAND, GRIP))
        }
    }

    @Test
    fun replaceEquippedItem() {
        runBlocking {
            val dagger = thing("Dagger") {
                equipTo(GRIP, RIGHT_HAND)
                equipTo(GRIP, LEFT_HAND)
            }.build()
            val hatchet = thing("Hatchet") {
                equipTo(GRIP, RIGHT_HAND)
                equipTo(GRIP, LEFT_HAND)
            }.build()

            val body = body("Test", RIGHT_HAND, LEFT_HAND)

            body.equip(dagger, RIGHT_HAND, GRIP)
            body.equip(hatchet, RIGHT_HAND, GRIP)

            assertEquals(1, body.getEquipped().size)
            assertEquals(1, body.getEquippedAt(GRIP).size)
            assertEquals(hatchet, body.getEquippedAt(RIGHT_HAND, GRIP))
            assertNull(body.getEquippedAt(LEFT_HAND, GRIP))
        }
    }

    @Test
    fun replaceOverlappedEquippedItem() {
        runBlocking {
            val shoe = thing("Shoe") {
                equipTo(CLOTHING, RIGHT_FOOT)
            }.build()
            val boot = thing("Boot") {
                equipTo(CLOTHING, RIGHT_FOOT, RIGHT_LEG)
            }.build()

            val body = body("Test", RIGHT_FOOT, RIGHT_LEG)

            body.equip(boot, boot.equipTargets.first())
            body.equip(shoe, shoe.equipTargets.first())

            assertEquals(1, body.getEquipped().size)
            assertEquals(1, body.getEquippedAt(CLOTHING).size)
            assertEquals(0, body.getEquippedOn(RIGHT_LEG).size)
            assertEquals(1, body.getEquippedOn(RIGHT_FOOT).size)
            assertEquals(shoe, body.getEquippedAt(RIGHT_FOOT, CLOTHING))
        }
    }
}
