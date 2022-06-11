package combat.block

import traveling.location.location.Location

class BlockHelper {
    var blockBodyPart: Location? = null
    val blockedBodyParts: MutableList<Location> = mutableListOf()

    fun resetStance() {
        blockedBodyParts.clear()
        blockBodyPart = null
    }
}