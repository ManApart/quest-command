package system

import core.gameState.Item
import core.utility.NameSearchableList

interface ItemParser {
    fun loadItems(): NameSearchableList<Item>
}