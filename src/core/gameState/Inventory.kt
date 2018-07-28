package core.gameState

import core.utility.NameSearchableList

class Inventory {
    private val items = NameSearchableList<Item>()

    fun itemExists(name: List<String>) : Boolean{
       return items.exists(name)
    }

    fun exists(item: Item) : Boolean{
       return items.exists(item)
    }

    fun getItem(name: List<String>) : Item {
        return items.get(name)
    }

    fun add(item: Item) {
        if (items.exists(item.name)){
            val match = items.get(item.name)
            if (item.isStackable(match)){
                match.count += item.count
            } else {
                items.add(item)
            }
        } else {
            items.add(item)
        }
    }

    fun remove(item: Item) {
        if (item.count > 1){
            item.count--
        } else {
            items.remove(item)
        }
    }

    fun getAllItems() : List<Item> {
        return items.toList()
    }
}