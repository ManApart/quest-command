package core.thing

import core.AIPackageStrings
import core.ai.*
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorRecipe
import core.ai.knowledge.CreatureMemory
import core.ai.knowledge.Mind
import core.ai.knowledge.MindP
import core.body.*
import core.body.BodyPartStrings.LEFT_HAND
import core.body.BodyPartStrings.LEFT_LEG
import core.body.BodyPartStrings.RIGHT_HAND
import core.body.BodyPartStrings.RIGHT_LEG
import core.body.BodyPartStrings.WAIST
import core.body.EquipLayerStrings.ARMOR
import core.body.EquipLayerStrings.CLOTHING
import core.body.EquipLayerStrings.GRIP
import core.properties.Properties
import core.properties.PropsBuilder
import core.properties.TagStrings
import core.properties.TagStrings.MEDIUM
import core.properties.TagStrings.SMALL
import core.properties.TagStrings.SOUND_DESCRIPTION
import core.properties.TagStrings.SOUND_LEVEL
import core.properties.ValueStrings.WEIGHT
import core.utility.MapBuilder
import core.utility.apply
import core.utility.applyNested
import core.utility.applySuspending
import crafting.material.DEFAULT_MATERIAL
import explore.listen.SOUND_LEVEL_DEFAULT
import inventory.Inventory
import status.Soul
import traveling.location.network.LocationNode
import traveling.location.network.NOWHERE_NODE
import traveling.position.NO_VECTOR
import traveling.position.Vector

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
    private val equipTargets = mutableListOf<EquipTarget>()
    private var ai: AI? = null
    private var mind: Mind? = null
    private var mindP: MindP? = null
    private var mindInitializer: Mind.() -> Unit = {}
    private var body: Body? = null
    private var bodyMaterial: String = DEFAULT_MATERIAL.name
    private var bodyName: String? = null
    private var bodyBuilder: (BodyBuilder.() -> Unit)? = null
    private var location: LocationNode? = null
    private var parent: Thing? = null
    private var position: Vector = NO_VECTOR
    private var inventory = Inventory()

    suspend fun build(additionalBases: List<ThingBuilder> = listOf(), tagsToApply: List<String> = listOf()): Thing {
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
        val body = discernBody(possibleBody, possibleBodyName, bodyBuilder, bodyMat)

        val allBehaviors = (behaviors + bases.flatMap { it.behaviors }).map { BehaviorManager.getBehavior(it) }
        val allItems = itemNames + bases.flatMap { it.itemNames }
        inventory.addAllByName(allItems, body)
        val ai = ai ?: basesR.firstNotNullOfOrNull { it.ai } ?: discernAI(props)
        val mindParsed = mindP?.let { Mind(ai, CreatureMemory(mindP!!.facts.map { it.parsed() }, mindP!!.listFacts.map { it.parsed() })) }
        val mind = this.mind ?: mindParsed ?: basesR.firstNotNullOfOrNull { it.mind } ?: Mind(ai)
        mind.mindInitializer()
        calcItemTargets(props)
        val equipTargets = (equipTargets + bases.flatMap { it.equipTargets }).toSet().toList()
        val loc = location ?: basesR.firstNotNullOfOrNull { it.location } ?: NOWHERE_NODE
        val pos = position.takeIf { it != NO_VECTOR } ?: basesR.firstNotNullOfOrNull { b -> b.position.takeIf { it != NO_VECTOR } } ?: NO_VECTOR

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
            equipTargets = equipTargets,
            inventory = inventory,
            properties = props,
        ).apply { position = pos }
    }

    private fun calcItemTargets(props: Properties) {
        if (props.isItem()) {
            val weight = props.values.getInt(WEIGHT, 100)
            if (props.tags.has(SMALL) || weight < 3) {
                equipToHoldOneHand()
            } else if (props.tags.has(MEDIUM) || weight < 6) {
                equipToHoldTwoHand()
            }
        }
    }

    suspend fun buildWithBase(builders: Map<String, ThingBuilder>, tagsToApply: List<String> = listOf()): Thing {
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
    suspend fun extends(other: Thing) = bases.add(unBuild(other))

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

    fun inventory(inventory: Inventory){
        this.inventory = inventory
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

    fun packageAI(packageName: String) {
        this.ai = PackageBasedAI(AIPackageManager.aiPackages[packageName]!!)
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

    fun body(body: Body) {
        this.body = body
    }

    fun body(name: String, initializer: BodyBuilder.() -> Unit = {}) {
        this.bodyName = name
        this.bodyBuilder = initializer
    }

    fun material(material: String) {
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

    fun position(pos: Vector) {
        this.position = pos
    }

    fun equipToHoldOneHand() = equipToEither(GRIP, RIGHT_HAND, LEFT_HAND)
    fun equipToHoldTwoHand() = equipTo(GRIP, RIGHT_HAND, LEFT_HAND)
    fun equipArmorPants() = equipTo(ARMOR, WAIST, RIGHT_LEG, LEFT_LEG)
    fun equipPants() = equipTo(CLOTHING, WAIST, RIGHT_LEG, LEFT_LEG)

    /**
    Equips to one part at this layer
     **/
    fun equipToEither(layer: Layer, vararg parts: String) {
        parts.forEach {
            equipTo(EquipTarget(layer, listOf(it)))
        }
    }

    /**
    Equips to all parts at this layer
     **/
    fun equipTo(layer: Layer, vararg parts: String) = equipTo(EquipTarget(layer, parts.toList()))

    fun equipTo(target: EquipTarget) = equipTargets.add(target)

    fun sound(description: String) {
        sound(SOUND_LEVEL_DEFAULT, description)
    }

    fun sound(level: Int, description: String) {
        propsBuilder.value(SOUND_DESCRIPTION, description)
        propsBuilder.value(SOUND_LEVEL, level)
    }

    private suspend fun unBuild(t: Thing): ThingBuilder {
        return thing(t.name) {
            description(t.description)
            location(t.location)
            t.parent?.let { parent(t.parent) }
            mind(Mind(t.mind.ai.copy(), CreatureMemory(t.mind.memory.getAllFacts(), t.mind.memory.getAllListFacts())))
            body(t.body.copy())
            t.equipTargets.forEach { equipTo(it) }
            item(t.inventory.getAllItems().map { it.name })
            props(t.properties)
            //This isn't including conditions etc
            soul(t.soul.getStats().map { it.name to it.level })
            behavior(t.behaviors.map { BehaviorRecipe(it.name, it.params) })
            param(t.params)
        }
    }

    private fun discernBody(possibleBody: Body?, possibleBodyName: String?, builder: (BodyBuilder.() -> Unit)?, bodyMat: String): Body {
        return when {
            possibleBody != null -> possibleBody
            possibleBodyName != null && builder != null -> BodyBuilder(possibleBodyName, bodyMat).apply(builder).build()
            else -> BodyManager.getBody(possibleBodyName!!)
        }
    }

    private fun discernAI(props: Properties): AI {
        return when {
            props.tags.has(TagStrings.PREDATOR) -> PackageBasedAI(AIPackageManager.aiPackages[AIPackageStrings.PREDATOR]!!)
            props.tags.has(TagStrings.COMMONER) -> PackageBasedAI(AIPackageManager.aiPackages[AIPackageStrings.PEASANT]!!)
            props.tags.has(TagStrings.CREATURE) -> PackageBasedAI(AIPackageManager.aiPackages[AIPackageStrings.CREATURE]!!)
            else -> DumbAI()
        }
    }

}

suspend fun thing(name: String, initializer: suspend ThingBuilder.() -> Unit): ThingBuilder {
    return ThingBuilder(name).applySuspending(initializer)
}
