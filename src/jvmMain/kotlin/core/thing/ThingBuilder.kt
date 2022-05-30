package core.thing

import core.ai.AI
import core.ai.AIManager
import core.ai.DumbAI
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorRecipe
import core.ai.knowledge.CreatureMemory
import core.ai.knowledge.Mind
import core.ai.knowledge.MindP
import core.body.Body
import core.body.BodyManager
import core.body.Slot
import core.properties.Properties
import core.properties.PropsBuilder
import core.utility.MapBuilder
import core.utility.apply
import core.utility.applyNested
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
    private var aiName: String? = null
    private var ai: AI? = null
    private var mind: Mind? = null
    private var mindP: MindP? = null
    private var body: Body? = null
    private var bodyName: String? = null
    private var location: LocationNode? = null
    private var parent: Thing? = null

    fun build(additionalBases: List<ThingBuilder> = listOf()): Thing {
        val bases = bases + additionalBases
        val basesR = bases.reversed()
        val params = paramsBuilder.build(bases.map { it.paramsBuilder })
        val props = propsBuilder.build(bases.map { it.propsBuilder }, params)
        val soulStats = soulBuilder.build(bases.map { it.soulBuilder }).mapValues { it.value.toInt() }
        val actualSoul = soulBuilt ?: Soul(soulStats)
        val desc = (description ?: basesR.firstNotNullOfOrNull { it.description } ?: "").apply(params)

        val possibleBodyName = (bodyName ?: basesR.firstNotNullOfOrNull { it.bodyName })
        val possibleBody = body ?: basesR.firstNotNullOfOrNull { it.body }
        val body = discernBody(possibleBody, possibleBodyName)

        val allBehaviors = (behaviors + bases.flatMap { it.behaviors }).map { BehaviorManager.getBehavior(it) }
        val allItems = itemNames + bases.flatMap { it.itemNames }
        val inventory = Inventory(name, body)
        inventory.addAllByName(allItems)
        val possibleAI = ai ?: basesR.firstNotNullOfOrNull { it.ai }
        val possibleAIName = aiName ?: basesR.firstNotNullOfOrNull { it.aiName }
        val ai = discernAI(possibleAI, possibleAIName)
        val mindParsed = mindP?.let { Mind(discernAI(possibleAI, mindP!!.aiName), CreatureMemory(mindP!!.personalFacts, mindP!!.personalListFacts, mindP!!.personalRelationships)) }
        val mind = this.mind ?: mindParsed ?: basesR.firstNotNullOfOrNull { it.mind } ?: Mind(ai)
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

    fun buildWithBase(builders: Map<String, ThingBuilder>): Thing {
        val bases = baseNames.map { builders[it]!! }
        return build(bases)
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

    fun ai(name: String) {
        this.aiName = name
    }

    fun ai(ai: AI) {
        this.ai = ai
    }

    fun mind(mind: Mind) {
        this.mind = mind
    }

    fun mind(mind: MindP) {
        this.mindP = mind
    }

    fun body(body: String) {
        this.bodyName = body
    }

    fun body(body: Body) {
        this.body = body
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
            mind(Mind(t.mind.ai, CreatureMemory(t.mind.memory.getAllFacts(), t.mind.memory.getAllListFacts(), t.mind.memory.getAllRelationships())))
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

    private fun discernAI(ai: AI?, aiName: String?): AI {
        return when {
            ai != null -> ai
            aiName != null -> AIManager.getAI(aiName)
            else -> DumbAI()
        }
    }

    private fun discernBody(possibleBody: Body?, possibleBodyName: String?): Body {
        return when {
            possibleBody != null -> possibleBody
            possibleBodyName != null -> BodyManager.getBody(possibleBodyName)
            else -> Body()
        }
    }

}

fun thing(name: String, initializer: ThingBuilder.() -> Unit): ThingBuilder {
    return ThingBuilder(name).apply(initializer)
}