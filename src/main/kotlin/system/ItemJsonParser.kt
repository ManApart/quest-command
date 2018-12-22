package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Item
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class ItemJsonParser : ItemParser {
    private val items by lazy{ NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/items", ::parseFile))}
    private fun parseFile(path: String): List<Item> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadItems(): NameSearchableList<Item> {
        return items
    }

}