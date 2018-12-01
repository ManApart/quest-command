package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator
import core.utility.JsonDirectoryParser

object ActivatorManager {
    val activators = JsonDirectoryParser.parseDirectory("/data/content/activators", ::parseFile)
    private fun parseFile(path: String): List<Activator> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun getActivator(name: String) : Activator {
        return activators.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getActivators(names: List<String>) : List<Activator> {
        return names.asSequence().map { getActivator(it) }.toList()
    }
}