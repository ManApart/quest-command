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
import status.stat.*
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
        return collection.values().build(CREATURE_TAG).toNameSearchableList()
    }

    private suspend fun getCreature(name: String): Thing {
        return thing(name) {
            extends(creatures.get().get(name))
        }.build().startingStats()
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
            }.build().startingStats()
                .also { thing -> it.transform(thing) }

            if (!it.location.isNullOrBlank()) {
                creature.properties.values.put("locationDescription", it.location)
            }
            creature.position = it.position
            creature
        }
    }

    private fun Thing.startingStats(): Thing {
        return this.apply {
            with(soul) {
                if (!hasStat(HEALTH)) addStat(HEALTH, 1, 10, 1)
                if (!hasStat(PERCEPTION)) addStat(PERCEPTION, 1, 1, 1)
                if (!hasStat(STAMINA)) addStat(STAMINA, 1, 100, 1)
                if (!hasStat(FOCUS)) addStat(FOCUS, 1, 100, 1)
                if (!hasStat(STRENGTH)) addStat(STRENGTH, 1, 1, 1)
            }
        }
    }

}