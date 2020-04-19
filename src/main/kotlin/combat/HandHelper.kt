package combat

import core.target.Target
import core.utility.NameSearchableList
import traveling.location.location.Location

class HandHelper(creature: Target, source: String, desiredSkill: String) {
    lateinit var hand: Location; private set
    var weapon: Target? = null

    init {
        determineHand(creature, source, desiredSkill)
    }

    private fun determineHand(creature: Target, source: String, desiredSkill: String) {
        val rightHand = creature.body.getPart("right hand")
        val leftHand = creature.body.getPart("left hand")
        val rightWeapon = rightHand.getEquippedItem("right hand grip")
        val leftWeapon = leftHand.getEquippedItem("left hand grip")
        val weapons = NameSearchableList<Target>()
        if (rightWeapon != null) weapons.add(rightWeapon)
        if (leftWeapon != null) weapons.add(leftWeapon)

        when {
            isHand(source) -> {
                hand = getHand(source, rightHand, leftHand)
                weapon = getWeapon(hand)
            }
            isWeapon(source, weapons) -> {
                weapon = getWeapon(source, weapons)
                hand = getHand(weapon!!, rightHand, leftHand)
            }
            leftWeapon?.properties?.values?.getInt(desiredSkill) ?: 0 > rightWeapon?.properties?.values?.getInt(desiredSkill) ?: 0 -> {
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

    }

    private fun isHand(source: String): Boolean {
        return source.isNotBlank() && listOf("left hand", "left", "l", "right", "r").contains(source)
    }

    private fun isWeapon(source: String, weapons: NameSearchableList<Target>): Boolean {
        return source.isNotBlank() && weapons.exists(source)
    }

    private fun getWeapon(source: String, weapons: NameSearchableList<Target>): Target {
        return weapons.get(source)
    }

    private fun getWeapon(source: Location): Target? {
        return source.getEquippedItem("right hand grip") ?: source.getEquippedItem("left hand grip")
    }

    private fun getHand(source: String, rightHand: Location, leftHand: Location): Location {
        return if (listOf("left", "l").contains(source)) {
            leftHand
        } else {
            rightHand
        }
    }

    private fun getHand(weapon: Target, rightHand: Location, leftHand: Location): Location {
        return if (weapon == getWeapon(rightHand)) {
            rightHand
        } else {
            leftHand
        }
    }

}