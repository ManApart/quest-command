package core.gameState.body

import core.gameState.Item

class Slot(val attachPoints: List<String>) {
    val description = attachPoints.joinToString(", ")

    override fun toString(): String {
        return description
    }

    fun getEquippedItems(body: Body) : List<Item> {
        val items = mutableListOf<Item>()
        attachPoints.forEach {
            items.addAll(body.getEquippedItemsAt(it))
        }
        return items
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