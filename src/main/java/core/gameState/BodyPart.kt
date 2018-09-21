package core.gameState

import core.utility.Named

class BodyPart(override val name: String) :Named {
    var equippedItem: Item? = null

    override fun toString(): String {
        return name
    }

    fun getDefense(): Int {
        return 0
    }

    fun isEquipped() : Boolean {
        return equippedItem != null
    }

    fun equippedName() : String {
            return equippedItem?.name ?: name
    }
}