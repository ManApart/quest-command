package core.body

import core.thing.Thing

data class Slot(val attachPoints: List<String>) {
    val description = attachPoints.joinToString(", ")

    override fun toString(): String {
        return description
    }

    fun getEquippedItems(body: Body) : List<Thing> {
        val items = mutableListOf<Thing>()
        attachPoints.forEach {
            items.addAll(body.getEquippedItemsAt(it))
        }
        return items
    }

    fun itemIsEquipped(item: Thing, body: Body) : Boolean {
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
        return attachPoints.firstOrNull { attachPoint.lowercase() == it.lowercase() } != null
    }

}