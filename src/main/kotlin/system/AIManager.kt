package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.AI
import core.gameState.AIBase
import core.gameState.Creature
import core.utility.JsonDirectoryParser

object AIManager {
    private val AIs = JsonDirectoryParser.parseDirectory("/data/content/ai", ::parseFile)
    private fun parseFile(path: String): List<AIBase> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun getAI(name: String, creature: Creature) : AI {
        return AIs.first { it.name.toLowerCase() == name.toLowerCase() }.create(creature)
    }

}