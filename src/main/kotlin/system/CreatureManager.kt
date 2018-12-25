package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Creature
import core.gameState.location.LocationTarget
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

object CreatureManager {
    private val creatures = NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/creatures", ::parseFile))
    private fun parseFile(path: String): List<Creature> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    private fun getCreature(name: String) : Creature {
        return Creature(creatures.get(name))
    }

    fun getCreatures(names: List<String>) : List<Creature> {
        return names.map { getCreature(it) }
    }

    fun getCreaturesFromLocationTargets(targets: List<LocationTarget>) : List<Creature> {
        return targets.map { Creature(creatures.get(it.name), it.params, it.location) }
    }

}