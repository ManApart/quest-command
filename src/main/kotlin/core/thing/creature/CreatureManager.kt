package core.thing.creature

import core.DependencyInjector
import core.startupLog
import core.thing.Thing
import core.thing.build
import core.thing.thing
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.location.LocationThing

const val CREATURE_TAG = "Creature"

object CreatureManager {
    private var creatures = loadCreatures()

    fun reset() {
        creatures = loadCreatures()
    }

    private fun loadCreatures(): NameSearchableList<Thing> {
        startupLog("Loading Creatures.")
        val collection = DependencyInjector.getImplementation(CreaturesCollection::class)
        return collection.values.build(CREATURE_TAG).toNameSearchableList()
    }

    private fun getCreature(name: String): Thing {
        return thing(name){
            extends(creatures.get(name))
        }.build()
    }

    fun getCreatures(names: List<String>): List<Thing> {
        return names.map { getCreature(it) }
    }

    fun getAllCreatures(): List<Thing> {
        return creatures.toList()
    }

    fun getCreaturesFromLocationThings(things: List<LocationThing>): List<Thing> {
        return things.map {
            val creature = thing(it.name){
                extends(creatures.get(it.name))
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