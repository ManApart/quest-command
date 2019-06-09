package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Target
import core.gameState.location.LocationTarget
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

object CreatureManager {
    private val creatures = NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/creatures", CreatureManager::parseFile))
    private fun parseFile(path: String): List<Target> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    init {
        creatures.map { it.properties.tags.add("Creature") }
    }

    private fun getCreature(name: String) : Target {
        return Target(name, creatures.get(name))
    }

    fun getCreatures(names: List<String>) : List<Target> {
        return names.map { getCreature(it) }
    }

    fun getCreaturesFromLocationTargets(targets: List<LocationTarget>) : List<Target> {
        return targets.map {
            val creature = Target(it.name, creatures.get(it.name), it.params)
            if (!it.location.isNullOrBlank()) {
                creature.properties.values.put("locationDescription", it.location)
            }
            creature
        }
    }

}