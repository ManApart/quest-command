package core.target

import core.ai.behavior.BehaviorRecipe
import core.body.Body
import core.body.BodyManager
import core.body.NONE
import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.PropsBuilder
import core.utility.MapBuilder

class TargetBuilder(internal val name: String) {
    private var propsBuilder = PropsBuilder()
    private var description: ConditionalStringPointer? = null
    private val params: MapBuilder = MapBuilder()
    private val behaviors = mutableListOf<BehaviorRecipe>()
    private val itemNames = mutableListOf<String>()
    //TODO - replace with body builder (grain chute)
    private var bodyName: String? = null
    private var body: Body? = null
    internal var baseName: String? = null

    fun extends(other: String){
        baseName = other
    }

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun params(vararg values: Pair<String, Any>) = this.params.entry(values.toList())
    fun params(key: String, value: String) = params.entry(key, value)
    fun params(key: String, value: Int) = params.entry(key, value)

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

    fun body(name: String){
        bodyName = name
    }

    fun body(body: Body){
        this.body = body
    }

    fun item(vararg itemName: String){
        itemNames.addAll(itemName)
    }

    fun build(base: TargetBuilder? = null): Target {
        val props = propsBuilder.build()
        val actualBody = body ?: bodyName?.let{ BodyManager.getBody(bodyName!!) } ?: NONE

        return Target(
            name,
            params = params.build(),
            dynamicDescription = description ?: ConditionalStringPointer(name),
            body = actualBody,
            properties = props,
        )
    }

}

fun target(name: String, initializer: TargetBuilder.() -> Unit): TargetBuilder {
    return TargetBuilder(name).apply(initializer)
}

fun List<TargetBuilder>.buildAll(): List<Target>{
    val builders = associateBy { it.name }
    return builders.values.map { buildTarget(it, builders) }.toList()
}

private fun buildTarget(builder: TargetBuilder, builders: Map<String, TargetBuilder>): Target {
    val base = builder.baseName?.let { builders[builder.baseName] }
    return builder.build(base)
}