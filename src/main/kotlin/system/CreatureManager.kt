package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Creature
import core.utility.JsonDirectoryParser

object CreatureManager {
    private val creatures = JsonDirectoryParser.parseDirectory("/data/content/creatures", ::parseFile)
    private fun parseFile(path: String): List<Creature> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    private fun getCreature(name: String) : Creature {
        return creatures.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getCreatures(names: List<String>) : List<Creature> {
        return names.asSequence().map { getCreature(it) }.toList()
    }
}