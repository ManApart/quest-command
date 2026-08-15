package combat.block

import core.body.BodyPart
import core.thing.Thing

//TODO - this class should be used
/*
Roll a success chance per block
If success, block body part takes damage instead of any of the blocked parts
Success is easier per block skill, lower the more parts you try to block
 */
class BlockHelper {
    var shield: Thing? = null
    var blockBodyPart: BodyPart? = null
    val blockedBodyParts: MutableList<BodyPart> = mutableListOf()

    fun resetStance() {
        blockedBodyParts.clear()
        blockBodyPart = null
        shield = null
    }
}
