package system.creature

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Target
import core.utility.JsonDirectoryParser

class CreatureJsonParser : CreatureParser {
    private fun parseFile(path: String): List<Target> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadCreatures(): List<Target> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/creatures", ::parseFile)
    }
}