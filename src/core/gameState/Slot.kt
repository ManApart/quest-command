package core.gameState

class Slot(val bodyParts: List<String>) {
    val description = bodyParts.joinToString(", ")

    fun itemIsEquipped(item: Item, body: Body) : Boolean {
        bodyParts.forEach {
            if (!body.hasPart(it) || body.getEquippedItemAt(it) != item) {
                return false
            }
        }
        return true
    }

    fun isEmpty(body: Body) : Boolean {
        bodyParts.forEach {
            if (!body.hasPart(it) || body.getEquippedItemAt(it) == null) {
                return false
            }
        }
        return true
    }

    fun contains(bodyPart: String) : Boolean {
        return bodyParts.firstOrNull { bodyPart.toLowerCase() == it.toLowerCase() } != null
    }

}