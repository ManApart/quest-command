package core.gameState

import com.fasterxml.jackson.annotation.JsonProperty
import core.events.Event
import core.gameState.behavior.BehaviorRecipe
import core.gameState.climb.Climbable
import core.gameState.inhertiable.InheritRecipe
import core.utility.apply
import system.BehaviorManager
import system.InheritableManager

class Activator(
        name: String,
        description: String = "Nothing interesting",
        override val locationDescription: String? = null,
        val climb: Climbable? = null,
        items: List<String> = listOf(),
        @JsonProperty("behaviors") val behaviorRecipes: MutableList<BehaviorRecipe> = mutableListOf(),
        properties: Properties = Properties(),
        inherits: List<InheritRecipe> = listOf(),
        val params: Map<String, String> = mapOf()
) : Target {

    constructor(base: Activator, params: Map<String, String> = mapOf(), locationDescription: String? = null) : this(
            base.name.apply(params),
            base.description.apply(params),
            (locationDescription
                    ?: base.locationDescription)?.apply(params),
            base.climb?.let { Climbable(it, params) },
            base.creature.inventory.getAllItems().map { it.name },
            base.behaviorRecipes.asSequence().map { BehaviorRecipe(it, params) }.toMutableList(),
            Properties(base.creature.properties, params)
    )

    val creature = Creature(name, description, parent = this, properties = properties, inventory = Inventory(items))
    override val name: String get() = creature.name
    override val description: String get() = creature.description
    override val properties: Properties get() = creature.properties

    init {
        applyInherits(inherits)
    }

    private val behaviors = BehaviorManager.getBehaviors(behaviorRecipes)

    init {
        properties.tags.remove("Creature")
    }

    private fun applyInherits(inherits: List<InheritRecipe>) {
        inherits.forEach { inheritRecipe ->
            val inherit = InheritableManager.getInheritable(inheritRecipe)
            val behaviorRecipeNames = behaviorRecipes.map { it.name }
            behaviorRecipes.addAll(inherit.behaviorRecipes.filter { !behaviorRecipeNames.contains(it.name) })
            properties.inherit(inherit.properties)
        }
    }

    override fun toString(): String {
        return name
    }

    fun evaluate(event: Event): Boolean {
        return behaviors.any { it.evaluate(event) }
    }

    fun evaluateAndExecute(event: Event) {
        behaviors.filter { it.evaluate(event) }
                .forEach { it.execute(this) }
    }

}

