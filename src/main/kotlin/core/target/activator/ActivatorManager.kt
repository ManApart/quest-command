package core.target.activator

import core.DependencyInjector
import core.target.Target
import core.target.activator.dsl.ActivatorsCollection
import core.target.build
import core.utility.NameSearchableList
import core.utility.Named
import core.utility.toNameSearchableList
import traveling.location.location.LocationTarget

const val ACTIVATOR_TAG = "Activator"

object ActivatorManager {
    private var activatorsCollection = DependencyInjector.getImplementation(ActivatorsCollection::class.java)

    private var activators = activatorsCollection.values.build()

    init {
        activators.forEach { it.properties.tags.add(ACTIVATOR_TAG) }
    }

    fun reset() {
        activatorsCollection = DependencyInjector.getImplementation(ActivatorsCollection::class.java)
        activators = activatorsCollection.values.build()
        activators.forEach { it.properties.tags.add(ACTIVATOR_TAG) }
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