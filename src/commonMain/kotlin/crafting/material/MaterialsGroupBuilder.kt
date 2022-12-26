package crafting.material

import core.ai.behavior.BehaviorRecipe
import core.properties.Properties
import core.properties.PropsBuilder

class MaterialsGroupBuilder {
    internal val children = mutableListOf<MaterialBuilder>()
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

    fun material(name: String, density: Int = 0, roughness: Int = 0, initializer: MaterialBuilder.() -> Unit = {}) {
        children.add(MaterialBuilder(name).apply {
            this.density = density
            this.roughness = roughness
            initializer()
        })
    }

    fun build(): List<Material> {
        return children.map { it.build(listOf(propsBuilder), behaviors) }
    }
}