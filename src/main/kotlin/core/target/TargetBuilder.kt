package core.target

import core.ai.behavior.BehaviorRecipe
import core.body.Body
import core.body.BodyBuilder
import core.body.BodyManager
import core.body.NONE
import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.PropsBuilder
import core.utility.MapBuilder

class TargetBuilder(internal val name: String) {
    private var propsBuilder = PropsBuilder()
    private var bodyBuilder = BodyBuilder()
    private var body: Body? = null
    private var description: ConditionalStringPointer? = null
    private val params: MapBuilder = MapBuilder()
    private val behaviors = mutableListOf<BehaviorRecipe>()
    private val itemNames = mutableListOf<String>()
    internal var baseName: String? = null

    fun extends(other: String){
        baseName = other
    }

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun param(vararg values: Pair<String, Any>) = this.params.entry(values.toList())
    fun param(key: String, value: String) = params.entry(key to value)
    fun param(key: String, value: Int) = params.entry(key to value.toString())

    fun description(desc: String) {
        description = ConditionalStringPointer(desc)
    }

    fun description(desc: String, type: ConditionalStringType) {
        description = ConditionalStringPointer(desc, type)
    }

    fun behavior(name: String, vararg params: Pair<String, Any>) {
        behaviors.add(BehaviorRecipe(name, params.associate { it.first to it.second.toString() }))
    }

    fun behavior(vararg recipes: BehaviorRecipe) {
        behaviors.addAll(recipes)
    }

    fun body(body: Body) {
        this.body = body
    }

    fun body(name: String = NONE.name, initializer: BodyBuilder.() -> Unit = {}) {
        bodyBuilder = BodyBuilder(name).apply(initializer)
    }

    fun item(vararg itemName: String){
        itemNames.addAll(itemName)
    }

    fun build(base: TargetBuilder? = null): Target {
        val props = propsBuilder.build()
        val body = this.body ?: bodyBuilder.build()

        return Target(
            name,
            params = params.build(),
            dynamicDescription = description ?: ConditionalStringPointer(name),
            behaviorRecipes = behaviors,
            body = body,
            properties = props,
        )
    }

    fun buildWithBase(builders: Map<String, TargetBuilder>): Target {
        val base = baseName?.let { builders[baseName] }
        return build(base)
    }

}

fun target(name: String, initializer: TargetBuilder.() -> Unit): TargetBuilder {
    return TargetBuilder(name).apply(initializer)
}