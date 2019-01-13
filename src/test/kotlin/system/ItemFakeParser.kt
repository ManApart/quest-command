package system

import core.gameState.Item
import core.utility.NameSearchableList
import system.item.ItemParser

class ItemFakeParser(items: List<Item> = listOf()) : ItemParser {
    private val items = NameSearchableList(items)

    override fun loadItems(): NameSearchableList<Item> {
        return items
    }
}