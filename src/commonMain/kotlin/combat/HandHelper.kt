package combat

import core.thing.Thing
import core.utility.NameSearchableList
import traveling.location.location.Location

class HandHelper(val hand: Location, val weapon: Thing?)

suspend fun handHelper(creature: Thing, source: String, desiredSkill: String): HandHelper {
    val rightHand = creature.body.getPart("right hand")
    val leftHand = creature.body.getPart("left hand")
    val rightWeapon = rightHand.getEquippedItem("right hand grip")
    val leftWeapon = leftHand.getEquippedItem("left hand grip")
    val weapons = NameSearchableList<Thing>()
    if (rightWeapon != null) weapons.add(rightWeapon)
    if (leftWeapon != null) weapons.add(leftWeapon)

    val hand: Location
    val weapon: Thing?

    when {
        isHand(source) -> {
            hand = getHand(source, rightHand, leftHand)
            weapon = getWeapon(hand)
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

private fun getWeapon(source: Location): Thing? {
    return source.getEquippedItem("right hand grip") ?: source.getEquippedItem("left hand grip")
}

private fun getHand(source: String, rightHand: Location, leftHand: Location): Location {
    return if (listOf("left", "l").contains(source)) {
        leftHand
    } else {
        rightHand
    }
}

private fun getHand(weapon: Thing, rightHand: Location, leftHand: Location): Location {
    return if (weapon == getWeapon(rightHand)) {
        rightHand
    } else {
        leftHand
    }
}