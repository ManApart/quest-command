package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

object ActivatorManager {
    private val activators = NameSearchableList(JsonDirectoryParser.parseDirectory("/data/content/activators", ::parseFile))
    private fun parseFile(path: String): List<Activator> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun getActivator(name: String) : Activator {
//        return activators.get(name)
        return Activator(activators.get(name))
    }

    fun getActivators(names: List<String>) : List<Activator> {
//        return activators.getAll(names)
        return activators.getAll(names).map { Activator(it) }
    }

    fun getAll() : NameSearchableList<Activator> {
        return NameSearchableList(activators)
    }
}