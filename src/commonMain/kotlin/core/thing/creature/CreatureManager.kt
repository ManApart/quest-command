package core.thing.creature

import core.DependencyInjector
import core.ai.AIManager
import core.startupLog
import core.thing.Thing
import core.thing.build
import core.thing.thing
import core.utility.Backer
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList
import traveling.location.location.LocationThing

const val CREATURE_TAG = "Creature"

object CreatureManager {
    private val creatures = Backer(::loadCreatures)
    suspend fun getCreatures() = creatures.get()

    suspend fun reset() {
        creatures.reset()
    }

    private suspend fun loadCreatures(): NameSearchableList<Thing> {
        startupLog("Loading Creatures.")
        val collection = DependencyInjector.getImplementation(CreaturesCollection::class)
        return collection.values.build(CREATURE_TAG).toNameSearchableList()
    }

    private suspend fun getCreature(name: String): Thing {
        return thing(name) {
            extends(getCreatures().get(name))
        }.build()
    }

    suspend fun getCreatures(names: List<String>): List<Thing> {
        return names.map { getCreature(it) }
    }

    suspend fun getAllCreatures(): List<Thing> {
        return getCreatures().toList()
    }

    suspend fun getCreaturesFromLocationThings(things: List<LocationThing>): List<Thing> {
        return things.map {
            val creature = thing(it.name) {
                extends(getCreatures().get(it.name))
                param(it.params)
            }.build()

            if (!it.location.isNullOrBlank()) {
                creature.properties.values.put("locationDescription", it.location)
            }
            creature.position = it.position
            creature
        }
    }

}