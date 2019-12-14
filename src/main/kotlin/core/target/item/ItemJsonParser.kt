package core.target.item

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.target.Target
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class ItemJsonParser : ItemParser {
    private fun parseFile(path: String): List<Target> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadItems(): NameSearchableList<Target> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/items", ::parseFile).asSequence().onEach { it.properties.tags.add("Item") }.toList())
    }

}