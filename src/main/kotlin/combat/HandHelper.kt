package combat

import core.gameState.BodyPart
import core.gameState.GameState
import core.gameState.Item
import core.utility.NameSearchableList

class HandHelper(source: String, desiredSkill: String) {
    lateinit var hand: BodyPart; private set
    private var weapon: Item? = null

    init {
        determineHand(source, desiredSkill)
    }

    private fun determineHand(source: String, desiredSkill: String) {
        val rightHand = GameState.player.creature.body.getPart("right hand")
        val leftHand = GameState.player.creature.body.getPart("left hand")
        val rightWeapon = rightHand.equippedItem
        val leftWeapon = leftHand.equippedItem
        val weapons = NameSearchableList<Item>()
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
            isHighestDamage(rightWeapon, leftWeapon, desiredSkill) -> {
                weapon = rightWeapon
                hand = rightHand
            }
            isHighestDamage(leftWeapon, rightWeapon, desiredSkill) -> {
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

    private fun isWeapon(source: String, weapons: NameSearchableList<Item>): Boolean {
        return source.isNotBlank() && weapons.exists(source)
    }

    private fun getWeapon(source: String, weapons: NameSearchableList<Item>): Item {
        return weapons.get(source)
    }

    private fun getWeapon(source: BodyPart): Item? {
        return source.equippedItem
    }

    private fun getHand(source: String, rightHand: BodyPart, leftHand: BodyPart): BodyPart {
        return if (listOf("left", "l").contains(source)) {
            leftHand
        } else {
            rightHand
        }
    }

    private fun getHand(weapon: Item, rightHand: BodyPart, leftHand: BodyPart): BodyPart {
        return if (weapon == rightHand.equippedItem) {
            rightHand
        } else {
            leftHand
        }
    }

    private fun isHighestDamage(expectedHigher: Item?, expectedLower: Item?, skill: String): Boolean {
        if (expectedHigher == null){
            return false
        }
        if (expectedHigher.properties.values.getInt(skill) == 0){
            return false
        }
        if (expectedLower == null){
            return true
        }
        if (expectedLower.properties.values.getInt(skill) == 0){
            return true
        }

        return expectedHigher.properties.values.getInt(skill) >= expectedLower.properties.values.getInt(skill)
    }


}