package core.gameState.bodies

import core.gameState.Item

class Slot(val bodyParts: List<String>) {
    val description = bodyParts.joinToString(", ")

    fun itemIsEquipped(body: Body) : Boolean {
        bodyParts.forEach {
            if (!body.hasPart(it) || body.getEquippedItemsAt(it).isNotEmpty()) {
                return false
            }
        }
        return true
    }
    fun itemIsEquipped(item: Item, body: Body) : Boolean {
        bodyParts.forEach {
            if (!body.hasPart(it) || !body.getEquippedItemsAt(it).contains(item)) {
                return false
            }
        }
        return true
    }

    fun isEmpty(body: Body) : Boolean {
        bodyParts.forEach {
            if (!body.hasPart(it) || body.getEquippedItemsAt(it).isNotEmpty()) {
                return false
            }
        }
        return true
    }

    fun contains(bodyPart: String) : Boolean {
        return bodyParts.firstOrNull { bodyPart.toLowerCase() == it.toLowerCase() } != null
    }

}