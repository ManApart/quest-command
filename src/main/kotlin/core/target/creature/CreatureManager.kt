package core.target.creature

import core.DependencyInjector
import core.target.Target
import core.utility.NameSearchableList
import traveling.location.location.LocationTarget

object CreatureManager {
    private var creatures = loadCreatures()

    fun reset() {
        creatures = loadCreatures()
    }

    private fun loadCreatures(): NameSearchableList<Target> {
        val parser = DependencyInjector.getImplementation(CreatureParser::class.java)
        val creatures = parser.loadCreatures()
        creatures.forEach { it.properties.tags.add("Creature") }
        return NameSearchableList(creatures)
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

            with(creature.properties.values) {
                put(core.properties.CAN_REST, true)
                put(core.properties.CAN_TRAVEL, true)
                put(core.properties.CAN_INTERACT, true)
            }

            if (!it.location.isNullOrBlank()) {
                creature.properties.values.put("locationDescription", it.location)
            }
            creature.position = it.position
            creature
        }
    }

}