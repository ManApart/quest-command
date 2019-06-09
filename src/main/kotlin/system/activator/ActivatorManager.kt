package system.activator

import core.gameState.Target
import core.gameState.location.LocationTarget
import core.utility.NameSearchableList
import system.DependencyInjector

object ActivatorManager {
    private var parser = DependencyInjector.getImplementation(ActivatorParser::class.java)

    private var activators = parser.loadActivators()

    fun reset() {
        parser = DependencyInjector.getImplementation(ActivatorParser::class.java)
        activators = parser.loadActivators()
        activators.map { it.properties.tags.add("Activator") }
    }

    fun getActivator(name: String): Target {
        return Target(name, activators.get(name))
    }

    fun getActivators(names: List<String>): List<Target> {
        return names.map { getActivator(it) }
    }

    fun getActivatorsFromLocationTargets(targets: List<LocationTarget>): List<Target> {
        return targets.map {
            val activator = Target(it.name, activators.get(it.name), it.params)
            if (!it.location.isNullOrBlank()) {
                activator.properties.values.put("locationDescription", it.location)
            }
            activator
        }
    }

    fun getAll(): NameSearchableList<Target> {
        return NameSearchableList(activators)
    }
}