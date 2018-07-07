package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator

object ActivatorManager {
    private val activators = loadActivators()

    private fun loadActivators(): List<Activator> {
        val json = this::class.java.classLoader.getResource("core/data/Activators.json").readText()
        return jacksonObjectMapper().readValue(json)
    }

    fun getActivator(name: String) : Activator {
        return activators.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getActivators(names: List<String>) : List<Activator> {
        return names.map { getActivator(it) }.toList()
    }
}