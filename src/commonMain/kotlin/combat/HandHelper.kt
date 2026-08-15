package combat

import core.body.BodyPart
import core.body.EquipLayerStrings.GRIP
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.toNameSearchableList

class HandHelper(val hand: BodyPart, val weapon: Thing?)

fun handHelper(creature: Thing, source: String, desiredSkill: String): HandHelper {
    val rightHand = creature.body2.parts.get("right hand")
    val leftHand = creature.body2.parts.get("left hand")
    val rightWeapon = rightHand.getEquipped(GRIP)
    val leftWeapon = leftHand.getEquipped(GRIP)
    val weapons = listOfNotNull(rightWeapon, leftWeapon).toNameSearchableList()

    val hand: BodyPart
    val weapon: Thing?

    when {
        isHand(source) -> {
            hand = getHand(source, rightHand, leftHand)
            weapon = hand.getEquipped(GRIP)
        }

        isWeapon(source, weapons) -> {
            weapon = getWeapon(source, weapons)
            hand = getHand(weapon, rightHand, leftHand)
        }

        (leftWeapon?.properties?.values?.getInt(desiredSkill) ?: 0) > (rightWeapon?.properties?.values?.getInt(desiredSkill) ?: 0) -> {
            weapon = leftWeapon
            hand = leftHand
        }

        rightWeapon != null -> {
            hand = rightHand
            weapon = rightWeapon
        }

        leftWeapon != null -> {
            weapon = leftWeapon
            hand = leftHand
        }

        else -> {
            hand = rightHand
            weapon = rightWeapon
        }
    }
    return HandHelper(hand, weapon)
}

private fun isHand(source: String): Boolean {
    return source.isNotBlank() && listOf("left hand", "left", "l", "right", "r").contains(source)
}

private fun isWeapon(source: String, weapons: NameSearchableList<Thing>): Boolean {
    return source.isNotBlank() && weapons.exists(source)
}

private fun getWeapon(source: String, weapons: NameSearchableList<Thing>): Thing {
    return weapons.get(source)
}

private fun getHand(source: String, rightHand: BodyPart, leftHand: BodyPart): BodyPart {
    return if (listOf("left", "l").contains(source)) leftHand else rightHand
}

private fun getHand(weapon: Thing, rightHand: BodyPart, leftHand: BodyPart): BodyPart {
    return if (weapon == rightHand.getEquipped(GRIP)) rightHand else leftHand
}
