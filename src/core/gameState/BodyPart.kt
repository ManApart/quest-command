package core.gameState

class BodyPart(val name: String) {
    var equippedItem: Item? = null

    fun getDefense(): Int {
        return 0
    }

    fun isEquipped() : Boolean {
        return equippedItem != null
    }
}