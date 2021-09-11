package core.target.creature

import core.DependencyInjector
import core.target.Target
import core.target.build
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import traveling.location.location.LocationTarget

const val CREATURE_TAG = "Creature"

object CreatureManager {
    private var creatures = loadCreatures()

    fun reset() {
        creatures = loadCreatures()
    }

    private fun loadCreatures(): NameSearchableList<Target> {
        val collection = DependencyInjector.getImplementation(CreaturesCollection::class.java)
        return collection.values.build(CREATURE_TAG).toNameSearchableList()
    }

    private fun getCreature(name: String): Target {
        return Target(name, creatures.get(name))
    }

    fun getCreatures(names: List<String>): List<Target> {
        return names.map { getCreature(it) }
    }

    fun getCreaturesFromLocationTargets(targets: List<LocationTarget>): List<Target> {
        return targets.map {
            val creature = Target(it.name, creatures.get(it.name), it.params)

            if (!it.location.isNullOrBlank()) {
                creature.properties.values.put("locationDescription", it.location)
            }
            creature.position = it.position
            creature
        }
    }

}