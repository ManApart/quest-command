package core.target

import core.ai.behavior.BehaviorRecipe
import core.body.Body
import core.body.BodyBuilder
import core.body.NONE
import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.PropsBuilder
import core.utility.MapBuilder

class TargetBuilder(internal val name: String) {
    private var propsBuilder = PropsBuilder()
    private var bodyBuilder = BodyBuilder()
    private var description: ConditionalStringPointer? = null
    private val params: MapBuilder = MapBuilder()
    private val behaviors = mutableListOf<BehaviorRecipe>()
    private val itemNames = mutableListOf<String>()
    private var baseNames = mutableListOf<String>()
    private var bases = mutableListOf<TargetBuilder>()
    private val slots = mutableListOf<List<String>>()

    /**
     * Note that each time this function is used, the latter extends object will win any extension conflicts.
     * extends(tree) - fire health 2
     * extends(burnable) - fire health 1
     * The end target will have fire health 1
     */
    fun extends(other: String) = baseNames.add(other)
    fun extends(other: TargetBuilder) = bases.add(other)

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

    fun behavior(vararg recipes: BehaviorRecipe) = behaviors.addAll(recipes)

    fun body(body: Body) {
        bodyBuilder.body(body)
    }

    fun body(name: String = NONE.name, initializer: BodyBuilder.() -> Unit = {}) {
        bodyBuilder = BodyBuilder(name).apply(initializer)
    }

    fun item(vararg itemName: String) = itemNames.addAll(itemName)

    fun equipSlot(vararg attachPoints: String) = slots.add(attachPoints.toList())

    fun build(base: TargetBuilder? = null): Target {
        val props = propsBuilder.build(base?.propsBuilder)
        val params = params.build(base?.params)
        val desc = description ?: base?.description ?: ConditionalStringPointer(name)
        val body = bodyBuilder.build(base?.bodyBuilder)
        val allBehaviors = behaviors + (base?.behaviors ?: emptyList())

        return Target(
            name,
            params = params,
            dynamicDescription = desc,
            behaviorRecipes = allBehaviors,
            body = body,
            equipSlots = this.slots,
            properties = props,
        )
    }

    fun build(bases: List<TargetBuilder>): Target {
        val props = propsBuilder.build(bases.map { it.propsBuilder })
        val params = params.build(bases.map { it.params })
        val desc = description ?: bases.firstNotNullOfOrNull { it.description } ?: ConditionalStringPointer(name)

        val body = bodyBuilder.build(bases.map { it.bodyBuilder })
        val allBehaviors = behaviors + bases.flatMap { it.behaviors }

        return Target(
            name,
            params = params,
            dynamicDescription = desc,
            behaviorRecipes = allBehaviors,
            body = body,
            equipSlots = this.slots,
            properties = props,
        )
    }

    fun buildWithBase(builders: Map<String, TargetBuilder>): Target {
        val bases = this.bases + baseNames.map { builders[it]!! }
        return build(bases)
    }

}

fun target(name: String, initializer: TargetBuilder.() -> Unit): TargetBuilder {
    return TargetBuilder(name).apply(initializer)
}