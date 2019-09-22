package system.ai

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.ai.AIBase
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class AIJsonParser : AIParser {
    private fun parseFile(path: String): List<AIBase> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadAI(): NameSearchableList<AIBase> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/ai", ::parseFile))
    }
}