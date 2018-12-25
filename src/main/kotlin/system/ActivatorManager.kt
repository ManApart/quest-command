package system

import core.gameState.Activator
import core.gameState.location.LocationTarget
import core.utility.NameSearchableList

object ActivatorManager {
    private var parser = DependencyInjector.getImplementation(ActivatorParser::class.java)

    private var activators = parser.loadActivators()

    fun reset() {
        parser = DependencyInjector.getImplementation(ActivatorParser::class.java)
        activators = parser.loadActivators()
    }

    fun getActivator(name: String): Activator {
        return Activator(activators.get(name))
    }

    fun getActivators(names: List<String>): List<Activator> {
        return names.map { getActivator(it) }
    }

    fun getActivatorsFromLocationTargets(targets: List<LocationTarget>): List<Activator> {
        return targets.map {
            Activator(activators.get(it.name), it.params, it.location)
        }
    }

    fun getAll(): NameSearchableList<Activator> {
        return NameSearchableList(activators)
    }
}