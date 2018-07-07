package core.gameState

class Inventory {
    val items = mutableListOf<Item>()

    fun itemExists(name: List<String>) : Boolean{
        if (name.isEmpty()) return false

        val fullName = name.joinToString(" ").toLowerCase()
        return items.firstOrNull { fullName.contains(it.name.toLowerCase()) } != null
    }

    fun getItem(name: String) : Item {
        return items.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getItem(name: List<String>) : Item {
        val fullName = name.joinToString(" ").toLowerCase()
        return items.first { fullName.contains(it.name.toLowerCase()) }
    }
}