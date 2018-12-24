package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator
import core.gameState.Item
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class ActivatorJsonParser : ActivatorParser {
    private val activators by lazy { NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/activators", ::parseFile)) }
    private fun parseFile(path: String): List<Activator> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadActivators(): NameSearchableList<Activator> {
        return activators
    }

}