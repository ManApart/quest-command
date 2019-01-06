package core.gameState.body

import core.gameState.Item

class Slot(val attachPoints: List<String>) {
    val description = attachPoints.joinToString(", ")

    fun itemIsEquipped(body: Body) : Boolean {
        attachPoints.forEach {
            if (body.getEquippedItemsAt(it).isNotEmpty()) {
                return false
            }
        }
        return true
    }
    fun itemIsEquipped(item: Item, body: Body) : Boolean {
        attachPoints.forEach {
            if (!body.getEquippedItemsAt(it).contains(item)) {
                return false
            }
        }
        return true
    }

    fun isEmpty(body: Body) : Boolean {
        attachPoints.forEach {
            if (body.getEquippedItemsAt(it).isNotEmpty()) {
                return false
            }
        }
        return true
    }

    fun contains(attachPoint: String) : Boolean {
        return attachPoints.firstOrNull { attachPoint.toLowerCase() == it.toLowerCase() } != null
    }

}