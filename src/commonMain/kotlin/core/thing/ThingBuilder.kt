package core.thing

import core.ai.*
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorRecipe
import core.ai.knowledge.CreatureMemory
import core.ai.knowledge.Mind
import core.ai.knowledge.MindP
import core.body.Body
import core.body.BodyCustomizer
import core.body.BodyManager
import core.body.Slot
import core.properties.Properties
import core.properties.PropsBuilder
import core.thing.creature.CREATURE_TAG
import core.utility.MapBuilder
import core.utility.apply
import core.utility.applyNested
import crafting.material.DEFAULT_MATERIAL
import crafting.material.Material
import crafting.material.MaterialManager
import explore.listen.SOUND_DESCRIPTION
import explore.listen.SOUND_LEVEL
import explore.listen.SOUND_LEVEL_DEFAULT
import inventory.Inventory
import status.Soul
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE

class ThingBuilder(internal val name: String) {
    private var propsBuilder = PropsBuilder()
    private var description: String? = null
    private val paramsBuilder = MapBuilder()
    private val soulBuilder = MapBuilder()
    private var soulBuilt: Soul? = null
    private val behaviors = mutableListOf<BehaviorRecipe>()
    private val itemNames = mutableListOf<String>()
    private var baseNames = mutableListOf<String>()
    private var bases = mutableListOf<ThingBuilder>()
    private val slots = mutableListOf<List<String>>()
    private var ai: AI? = null
    private var mind: Mind? = null
    private var mindP: MindP? = null
    private var mindInitializer: Mind.() -> Unit = {}
    private var body: Body? = null
    private var bodyMaterial: String = DEFAULT_MATERIAL.name
    private var bodyName: String? = null
    private var location: LocationNode? = null
    private var parent: Thing? = null
    private var bodyCustomizer: BodyCustomizer = BodyCustomizer()

    fun build(additionalBases: List<ThingBuilder> = listOf(), tagsToApply: List<String> = listOf()): Thing {
        val bases = bases + additionalBases
        val basesR = bases.reversed()
        val params = paramsBuilder.build(bases.map { it.paramsBuilder })
        val props = propsBuilder.build(bases.map { it.propsBuilder }, params).apply { tags.addAll(tagsToApply) }
        val soulStats = soulBuilder.build(bases.map { it.soulBuilder }).mapValues { it.value.toInt() }
        val actualSoul = soulBuilt ?: Soul(soulStats)
        val desc = (description ?: basesR.firstNotNullOfOrNull { it.description } ?: "").apply(params)

        val possibleBodyName = (bodyName ?: basesR.firstNotNullOfOrNull { it.bodyName })
        val possibleBody = body ?: basesR.firstNotNullOfOrNull { it.body }
        val bodyMat = (listOf(bodyMaterial) + basesR.map { it.bodyMaterial }).firstOrNull { it != DEFAULT_MATERIAL.name } ?: DEFAULT_MATERIAL.name
        val body = discernBody(possibleBody, possibleBodyName, MaterialManager.getMaterial(bodyMat), bodyCustomizer)

        val allBehaviors = (behaviors + bases.flatMap { it.behaviors }).map { BehaviorManager.getBehavior(it) }
        val allItems = itemNames + bases.flatMap { it.itemNames }
        val inventory = Inventory(name, body)
        inventory.addAllByName(allItems)
        val ai = ai ?: basesR.firstNotNullOfOrNull { it.ai } ?: if (tagsToApply.contains(CREATURE_TAG)) ConditionalAI() else DumbAI()
        val mindParsed = mindP?.let { Mind(ai, CreatureMemory(mindP!!.facts.map { it.parsed() }, mindP!!.listFacts.map { it.parsed() })) }
        val mind = this.mind ?: mindParsed ?: basesR.firstNotNullOfOrNull { it.mind } ?: Mind(ai)
        mind.mindInitializer()
        val equipSlots = ((slots + bases.flatMap { it.slots }).applyNested(params).map { Slot(it) } + calcHeldSlots(props)).toSet().toList()
        val loc = location ?: basesR.firstNotNullOfOrNull { it.location } ?: NOWHERE_NODE

        return Thing(
            name,
            desc,
            loc,
            parent,
            mind = mind,
            params = params,
            soul = actualSoul,
            behaviors = allBehaviors,
            body = body,
            equipSlots = equipSlots,
            inventory = inventory,
            properties = props,
        )
    }

    private fun calcHeldSlots(props: Properties): List<Slot> {
        return when {
            props.tags.has("Small") || props.values.getInt("weight", 100) < 3 -> listOf(Slot(listOf("Right Hand Grip")), Slot(listOf("Left Hand Grip")))
            props.tags.has("Medium") || props.values.getInt("weight", 100) < 6 -> listOf(Slot(listOf("Right Hand Grip", "Left Hand Grip")))
            else -> listOf()
        }
    }

    fun buildWithBase(builders: Map<String, ThingBuilder>, tagsToApply: List<String> = listOf()): Thing {
        val bases = baseNames.map { builders[it]!! }
        return build(bases, tagsToApply)
    }

    /**
     * Note that each time this function is used, the latter extends object will win any extension conflicts.
     * extends(tree) - fire health 2
     * extends(burnable) - fire health 1
     * The end thing will have fire health 1
     */
    fun extends(other: String) = baseNames.add(other)
    fun extends(other: ThingBuilder) = bases.add(other)
    fun extends(other: Thing) = bases.add(unBuild(other))

    fun props(initializer: PropsBuilder.() -> Unit) {
        propsBuilder.apply(initializer)
    }

    fun props(properties: Properties) {
        propsBuilder.props(properties)
    }

    fun param(vararg values: Pair<String, Any>) = this.paramsBuilder.entry(values.toList())
    fun param(values: Map<String, Any>) = this.paramsBuilder.entry(values.toList())
    fun param(key: String, value: String) = paramsBuilder.entry(key, value)
    fun param(key: String, value: Int) = paramsBuilder.entry(key, value)

    fun soul(vararg values: Pair<String, Any>) = this.soulBuilder.entry(values.toList())
    fun soul(values: List<Pair<String, Any>>) = this.soulBuilder.entry(values.toList())
    fun soul(key: String, value: String) = soulBuilder.entry(key, value)
    fun soul(key: String, value: Int) = soulBuilder.entry(key, value)
    fun soul(soul: Soul) {
        this.soulBuilt = soul
    }

    fun description(desc: String) {
        description = desc
    }

    fun behavior(name: String, vararg params: Pair<String, Any>) {
        behaviors.add(BehaviorRecipe(name, params.associate { it.first to it.second.toString() }))
    }

    fun behavior(vararg recipes: BehaviorRecipe) = behaviors.addAll(recipes)
    fun behavior(recipes: List<BehaviorRecipe>) = behaviors.addAll(recipes)

    fun playerAI() {
        this.ai = PlayerControlledAI()
    }

    fun conditionalAI() {
        this.ai = ConditionalAI()
    }

    fun dumbAI() {
        this.ai = DumbAI()
    }

    fun ai(ai: AI) {
        this.ai = ai
    }

    fun mind(mind: Mind, initializer: Mind.() -> Unit = {}) {
        this.mind = mind
        this.mindInitializer = initializer
    }

    fun mind(mind: MindP) {
        this.mindP = mind
    }

    fun mind(initializer: Mind.() -> Unit = {}) {
        this.mindInitializer = initializer
    }

    fun body(body: String, initializer: BodyCustomizer.() -> Unit = {}) {
        this.bodyName = body
        this.bodyCustomizer = BodyCustomizer().apply(initializer)
    }

    fun body(body: Body, initializer: BodyCustomizer.() -> Unit = {}) {
        this.body = body
        this.bodyCustomizer = BodyCustomizer().apply(initializer)
    }

    fun material(material: String){
        this.bodyMaterial = material
    }

    fun location(location: LocationNode) {
        this.location = location
    }

    fun item(vararg itemName: String) = itemNames.addAll(itemName)
    fun item(itemNames: List<String>) = this.itemNames.addAll(itemNames)

    fun parent(parent: Thing) {
        this.parent = parent
    }

    /**
     * Each string is a single attach point for a given slot. 5 strings = 5 slots
     */
    fun equipSlotOptions(vararg equipSlots: String) = equipSlots.forEach { slots.add(listOf(it)) }

    /**
     * Each list of strings creates a single equip slot. 5 lists = 5 slots
     */
    fun equipSlotOptions(vararg equipSlots: List<String>) = slots.addAll(equipSlots.map { it.toList() })

    fun equipSlotOptions(equipSlots: List<Slot>) = slots.addAll(equipSlots.map { it.attachPoints })

    /**
     * Add a single equip slot, with each string being an attach point in that slot.
     * This means that an equip slot with 5 attachment points will require all 5 points to be empty in order to equip the item
     */
    fun equipSlot(vararg attachPoints: String) = slots.add(attachPoints.toList())

    fun sound(description: String) {
        sound(SOUND_LEVEL_DEFAULT, description)
    }

    fun sound(level: Int, description: String) {
        propsBuilder.value(SOUND_DESCRIPTION, description)
        propsBuilder.value(SOUND_LEVEL, level)
    }

    private fun unBuild(t: Thing): ThingBuilder {
        return thing(t.name) {
            description(t.description)
            location(t.location)
            t.parent?.let { parent(t.parent) }
            mind(Mind(t.mind.ai, CreatureMemory(t.mind.memory.getAllFacts(), t.mind.memory.getAllListFacts())))
            body(Body(t.body))
            equipSlotOptions(t.equipSlots)
            item(t.inventory.getAllItems().map { it.name })
            props(t.properties)
            //This isn't including conditions etc
            soul(t.soul.getStats().map { it.name to it.level })
            behavior(t.behaviors.map { BehaviorRecipe(it.name, it.params) })
            param(t.params)
        }
    }

    private fun discernBody(possibleBody: Body?, possibleBodyName: String?, bodyMaterial: Material, bodyCustomizer: BodyCustomizer): Body {
        return when {
            possibleBody != null -> possibleBody
            possibleBodyName != null -> BodyManager.getBody(possibleBodyName)
            else -> Body(material = bodyMaterial)
        }.also { bodyCustomizer.apply(it) }
    }

}

fun thing(name: String, initializer: ThingBuilder.() -> Unit): ThingBuilder {
    return ThingBuilder(name).apply(initializer)
}