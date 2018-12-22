package system

import core.gameState.Item
import core.utility.NameSearchableList

class ItemFakeParser(items: List<Item> = listOf()) : ItemParser {
    private val items = NameSearchableList(items)

    override fun loadItems(): NameSearchableList<Item> {
        return items
    }
}