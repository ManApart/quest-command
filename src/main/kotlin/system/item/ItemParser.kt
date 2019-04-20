package system.item

import core.gameState.Target
import core.utility.NameSearchableList

interface ItemParser {
    fun loadItems(): NameSearchableList<Target>
}