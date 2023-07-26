package core.thing.activator

import core.DependencyInjector
import core.startupLog
import core.thing.Thing
import core.thing.activator.dsl.ActivatorsCollection
import core.thing.build
import core.thing.thing
import core.utility.Backer
import core.utility.NameSearchableList
import traveling.location.location.LocationThing

const val ACTIVATOR_TAG = "Activator"

object ActivatorManager {
    private val activators = Backer(::loadActivators)
    suspend fun getActivators() = activators.get()

    private suspend fun loadActivators(): NameSearchableList<Thing> {
        startupLog("Loading Activators.")
        val activatorsCollection = DependencyInjector.getImplementation(ActivatorsCollection::class)
        return activatorsCollection.values().build(ACTIVATOR_TAG)
    }

    suspend fun reset() {
        activators.reset()
    }

    suspend fun getActivator(name: String): Thing {
        return thing(name) {
            extends(getActivators().get(name))
        }.build()
    }

    suspend fun getActivators(names: List<String>): List<Thing> {
        return names.map { getActivator(it) }
    }

    suspend fun getActivatorsFromLocationThings(things: List<LocationThing>): List<Thing> {
        return things.map {
            val activator = thing(it.name) {
                extends(getActivators().get(it.name))
                param(it.params)
            }.build()
            if (!it.location.isNullOrBlank()) {
                activator.properties.values.put("locationDescription", it.location)
            }
            activator.position = it.position
            activator
        }
    }

    suspend fun getAll(): NameSearchableList<Thing> {
        return NameSearchableList(getActivators())
    }
}