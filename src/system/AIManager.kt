package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.AI
import core.gameState.AIBase
import core.gameState.Creature

object AIManager {
    private val AIs = loadAI()

    private fun loadAI(): List<AIBase> {
        val json = this::class.java.getResourceAsStream("/resource/data/AI.json")
        return jacksonObjectMapper().readValue(json)
    }

    fun getAI(name: String, creature: Creature) : AI {
        return AIs.first { it.name.toLowerCase() == name.toLowerCase() }.create(creature)
    }

}