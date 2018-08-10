package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Creature

object CreatureManager {
    private val creatures = loadCreatures()

    private fun loadCreatures(): List<Creature> {
        val json = this::class.java.classLoader.getResource("core/data/Creatures.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    fun getCreature(name: String) : Creature {
        return creatures.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getCreatures(names: List<String>) : List<Creature> {
        return names.map { getCreature(it) }.toList()
    }
}