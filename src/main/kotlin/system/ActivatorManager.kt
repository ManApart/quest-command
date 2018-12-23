package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator
import core.gameState.location.LocationTarget
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

object ActivatorManager {
    private val activators = NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/activators", ::parseFile))
    private fun parseFile(path: String): List<Activator> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun getActivator(name: String) : Activator {
        return Activator(activators.get(name))
    }

    fun getActivators(names: List<String>) : List<Activator> {
        return activators.getAll(names).map { Activator(it) }
    }

    fun getActivatorsFromLocationTargets(targets: List<LocationTarget>) : List<Activator> {
        return targets.map { Activator(activators.get(it.name), it.location) }
    }

    fun getAll() : NameSearchableList<Activator> {
        return NameSearchableList(activators)
    }
}