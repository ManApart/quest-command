package core.target.activator

import core.DependencyInjector
import core.target.Target
import core.target.activator.dsl.ActivatorsCollection
import core.target.build
import core.target.target
import core.utility.NameSearchableList
import traveling.location.location.LocationTarget

const val ACTIVATOR_TAG = "Activator"

object ActivatorManager {
    private var activators = loadActivators()

    private fun loadActivators(): NameSearchableList<Target>{
        val activatorsCollection = DependencyInjector.getImplementation(ActivatorsCollection::class)
        return activatorsCollection.values.build(ACTIVATOR_TAG)
    }

    fun reset() {
        activators = loadActivators()
    }

    fun getActivator(name: String): Target {
        return target(name){
            extends(activators.get(name))
        }.build()
    }

    fun getActivators(names: List<String>): List<Target> {
        return names.map { getActivator(it) }
    }

    fun getActivatorsFromLocationTargets(targets: List<LocationTarget>): List<Target> {
        return targets.map {
            val activator = target(it.name){
                extends(activators.get(it.name))
                param(it.params)
            }.build()
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