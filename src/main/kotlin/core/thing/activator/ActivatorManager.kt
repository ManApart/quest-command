package core.thing.activator

import core.DependencyInjector
import core.GameState
import core.VERBOSE_STARTUP
import core.startupLog
import core.thing.Thing
import core.thing.activator.dsl.ActivatorsCollection
import core.thing.build
import core.thing.thing
import core.utility.NameSearchableList
import traveling.location.location.LocationThing

const val ACTIVATOR_TAG = "Activator"

object ActivatorManager {
    private var activators = loadActivators()

    private fun loadActivators(): NameSearchableList<Thing>{
        startupLog("Loading Activators.")
        val activatorsCollection = DependencyInjector.getImplementation(ActivatorsCollection::class)
        return activatorsCollection.values.build(ACTIVATOR_TAG)
    }

    fun reset() {
        activators = loadActivators()
    }

    fun getActivator(name: String): Thing {
        return thing(name){
            extends(activators.get(name))
        }.build()
    }

    fun getActivators(names: List<String>): List<Thing> {
        return names.map { getActivator(it) }
    }

    fun getActivatorsFromLocationThings(things: List<LocationThing>): List<Thing> {
        return things.map {
            val activator = thing(it.name){
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

    fun getAll(): NameSearchableList<Thing> {
        return NameSearchableList(activators)
    }
}