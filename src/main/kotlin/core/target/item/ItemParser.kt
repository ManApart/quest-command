package core.target.item

import core.target.Target
import core.utility.NameSearchableList

interface ItemParser {
    fun loadItems(): NameSearchableList<Target>
}