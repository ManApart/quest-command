package core.target.activator

import core.DependencyInjector
import core.target.Target
import core.utility.NameSearchableList
import traveling.location.location.LocationTarget

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
            activator.position = it.position
            activator
        }
    }

    fun getAll(): NameSearchableList<Target> {
        return NameSearchableList(activators)
    }
}