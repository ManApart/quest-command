package crafting.material

import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorRecipe
import core.properties.Properties
import core.properties.PropsBuilder
import magic.Element

class MaterialBuilder(val name: String) {
    var density = 0
    var roughness = 0
    var interaction: (Element) -> Int = { 0 }
    private var propsBuilder = PropsBuilder()
    private val behaviors = mutableListOf<BehaviorRecipe>()

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun props(properties: Properties) {
        propsBuilder.props(properties)
    }

    fun behavior(name: String, vararg params: Pair<String, Any>) {
        behaviors.add(BehaviorRecipe(name, params.associate { it.first to it.second.toString() }))
    }

    fun behavior(vararg recipes: BehaviorRecipe) = behaviors.addAll(recipes)
    fun behavior(recipes: List<BehaviorRecipe>) = behaviors.addAll(recipes)


    fun build(): Material {
        return Material(name, density, roughness, propsBuilder.build(), behaviors.map { BehaviorManager.getBehavior(it) }, interaction)
    }
}