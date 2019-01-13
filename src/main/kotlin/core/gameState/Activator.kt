package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.behavior.BehaviorRecipe
import core.gameState.climb.Climbable
import core.gameState.location.LocationNode
import core.utility.apply
import system.behavior.BehaviorManager

class Activator(
        name: String,
        description: String = "Nothing interesting",
        params: Map<String, String> = mapOf(),
        climb: Climbable? = null,
        items: List<String> = listOf(),
        @JsonProperty("behaviors") behaviorRecipes: MutableList<BehaviorRecipe> = mutableListOf(),
        properties: Properties = Properties()

) : Target {
    constructor(base: Activator, params: Map<String, String> = mapOf()) : this(
            base.name,
            base.description,
            params,
            base.climb,
            base.creature.inventory.getItems().map { it.name },
            base.behaviorRecipes,
            base.creature.properties
    )

    val creature = Creature(name.apply(params), description.apply(params), parent = this, properties = Properties(properties, params), inventory = Inventory(items))
    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val inventory: Inventory get() = creature.inventory
    override val location: LocationNode get() = creature.location
    override val properties: Properties get() = creature.properties
    val climb = climb?.let { Climbable(it, params) }
    val behaviorRecipes = behaviorRecipes.asSequence().map { BehaviorRecipe(it, params) }.toMutableList()
    private val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)

    init {
        this.properties.tags.remove("Creature")
    }

    override fun toString(): String {
        return name
    }

    override fun canConsume(event: Event): Boolean {
        return behaviors.any { it.evaluate(event) }
    }

    override fun consume(event: Event) {
        behaviors.filter { it.evaluate(event) }
                .forEach { it.execute(event, this) }
    }

}

