package system

import core.target.Target
import core.utility.NameSearchableList
import core.target.item.ItemParser

class ItemFakeParser(items: List<Target> = listOf()) : ItemParser {
    private val items = NameSearchableList(items.onEach { it.properties.tags.add("Item") })

    override fun loadItems(): NameSearchableList<Target> {
        return items
    }
}