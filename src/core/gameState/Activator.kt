package core.gameState

import core.events.Event
import core.gameState.climb.Climbable

class Activator(name: String, description: String, val climb: Climbable?, private val triggers: List<Trigger> = listOf(), properties: Properties = Properties()) : Target {
    val creature = Creature(name, description, parent = this, properties = properties)

    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val properties: Properties get() = creature.properties

    init {
        properties.tags.remove("Creature")
    }

    override fun toString(): String {
        return name
    }

    fun evaluateAndExecute(event: Event) {
        triggers.forEach { it.evaluateAndExecute(this, event) }
    }

}

