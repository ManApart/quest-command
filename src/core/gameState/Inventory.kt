package core.gameState

import core.utility.NameSearchableList

class Inventory {
    val items = NameSearchableList<Item>()

    fun itemExists(name: List<String>) : Boolean{
       return items.exists(name)
    }

    fun getItem(name: List<String>) : Item {
        return items.get(name)
    }
}