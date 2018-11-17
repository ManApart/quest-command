package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.Activator

object ActivatorManager {
    val activators = loadActivators()

    private fun loadActivators(): List<Activator> {
        val json = this::class.java.getResourceAsStream("/data/Activators.json")
        return jacksonObjectMapper().readValue(json)
    }

    fun getActivator(name: String) : Activator {
        return activators.first { it.name.toLowerCase() == name.toLowerCase() }
    }

    fun getActivators(names: List<String>) : List<Activator> {
        return names.asSequence().map { getActivator(it) }.toList()
    }
}