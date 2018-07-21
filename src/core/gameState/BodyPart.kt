package core.gameState

import core.utility.Named

class BodyPart(override val name: String) :Named {
    var equippedItem: Item? = null

    fun getDefense(): Int {
        return 0
    }

    fun isEquipped() : Boolean {
        return equippedItem != null
    }
}