package combat

import core.gameState.body.BodyPart
import core.gameState.GameState
import core.gameState.Target
import core.utility.NameSearchableList

class HandHelper(source: String, desiredSkill: String) {
    lateinit var hand: BodyPart; private set
    var weapon: Target? = null

    init {
        determineHand(source, desiredSkill)
    }

    private fun determineHand(source: String, desiredSkill: String) {
        val rightHand = GameState.player.body.getPart("right hand")
        val leftHand = GameState.player.body.getPart("left hand")
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

    private fun getWeapon(source: BodyPart): Target? {
        return source.getEquippedItem("right hand grip") ?: source.getEquippedItem("left hand grip")
    }

    private fun getHand(source: String, rightHand: BodyPart, leftHand: BodyPart): BodyPart {
        return if (listOf("left", "l").contains(source)) {
            leftHand
        } else {
            rightHand
        }
    }

    private fun getHand(weapon: Target, rightHand: BodyPart, leftHand: BodyPart): BodyPart {
        return if (weapon == getWeapon(rightHand)) {
            rightHand
        } else {
            leftHand
        }
    }

}