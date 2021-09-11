package core.target

import core.ai.AI
import core.ai.behavior.BehaviorRecipe
import core.conditional.ConditionalStringPointer
import core.conditional.ConditionalStringType
import core.properties.PropsBuilder
import core.utility.MapBuilder
import status.ProtoSoul

class TargetBuilder(internal val name: String) {
    private var propsBuilder = PropsBuilder()
    private var description: ConditionalStringPointer? = null
    private val paramsBuilder = MapBuilder()
    private val soulBuilder = MapBuilder()
    private val behaviors = mutableListOf<BehaviorRecipe>()
    private val itemNames = mutableListOf<String>()
    private var baseNames = mutableListOf<String>()
    private var bases = mutableListOf<TargetBuilder>()
    private val slots = mutableListOf<List<String>>()
    private var aiName: String? = null
    private var ai: AI? = null
    private var bodyName: String? = null

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

    fun param(vararg values: Pair<String, Any>) = this.paramsBuilder.entry(values.toList())
    fun param(key: String, value: String) = paramsBuilder.entry(key, value)
    fun param(key: String, value: Int) = paramsBuilder.entry(key, value)

    fun soul(vararg values: Pair<String, Any>) = this.soulBuilder.entry(values.toList())
    fun soul(key: String, value: String) = soulBuilder.entry(key, value)
    fun soul(key: String, value: Int) = soulBuilder.entry(key, value)

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

    fun ai(name: String) {
        this.aiName = name
    }

    fun ai(ai: AI){
        this.ai = ai
    }

    fun body(body: String) {
        this.bodyName = body
    }

    fun item(vararg itemName: String) = itemNames.addAll(itemName)

    /**
     * Each string is a single attach point for a given slot. 5 strings = 5 slots
     */
    fun equipSlotOptions(vararg equipSlots: String) = equipSlots.forEach { slots.add(listOf(it)) }

    /**
     * Each list of strings creates a single equip slot. 5 lists = 5 slots
     */
    fun equipSlotOptions(vararg equipSlots: List<String>) = slots.addAll(equipSlots.map { it.toList() })

    /**
     * Add a single equip slot, with each string being an attach point in that slot.
     * This means that an equip slot with 5 attachment points will require all 5 points to be empty in order to equip the item
     */
    fun equipSlot(vararg attachPoints: String) = slots.add(attachPoints.toList())

    fun build(base: TargetBuilder? = null): Target {
        val props = propsBuilder.build(base?.propsBuilder)
        val params = paramsBuilder.build(base?.paramsBuilder)
        val soul = soulBuilder.build(base?.soulBuilder).mapValues { it.value.toInt() }
        val desc = description ?: base?.description ?: ConditionalStringPointer(name)
        val body = bodyName ?: base?.bodyName
        val allBehaviors = behaviors + (base?.behaviors ?: emptyList())
        val allItems = itemNames + (base?.itemNames ?: listOf())

        return Target(
            name,
            params = params,
            ai = ai ?: base?.ai,
            aiName = aiName ?: base?.aiName,
            soulStats = ProtoSoul(soul),
            dynamicDescription = desc,
            behaviorRecipes = allBehaviors,
            bodyName = body,
            equipSlots = this.slots,
            items = allItems,
            properties = props,
        )
    }

    fun build(bases: List<TargetBuilder>): Target {
        val basesR = bases.reversed()
        val props = propsBuilder.build(bases.map { it.propsBuilder })
        val params = paramsBuilder.build(bases.map { it.paramsBuilder })
        val soul = soulBuilder.build(bases.map { it.soulBuilder }).mapValues { it.value.toInt() }
        val desc = description ?: basesR.firstNotNullOfOrNull { it.description } ?: ConditionalStringPointer(name)

        val body = bodyName ?: basesR.firstNotNullOfOrNull { it.bodyName }
        val allBehaviors = behaviors + bases.flatMap { it.behaviors }
        val allItems = itemNames + bases.flatMap { it.itemNames }

        return Target(
            name,
            params = params,
            ai = ai ?: basesR.firstNotNullOfOrNull { it.ai },
            aiName = aiName ?: basesR.firstNotNullOfOrNull { it.aiName },
            soulStats = ProtoSoul(soul),
            dynamicDescription = desc,
            behaviorRecipes = allBehaviors,
            bodyName = body,
            equipSlots = this.slots,
            items = allItems,
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