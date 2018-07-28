package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.behavior.BehaviorRecipe
import core.gameState.climb.Climbable
import system.BehaviorManager

class Activator(name: String, description: String, val climb: Climbable?, @JsonProperty("behaviors") private val behaviorRecipes: List<BehaviorRecipe> = listOf(), properties: Properties = Properties()) : Target {
    val creature = Creature(name, description, parent = this, properties = properties)

    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val properties: Properties get() = creature.properties
    val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)

    init {
        properties.tags.remove("Creature")
    }

    override fun toString(): String {
        return name
    }

    fun evaluateAndExecute(event: Event) {
        behaviors.forEach { it.evaluateAndExecute(this, event) }
    }

}

