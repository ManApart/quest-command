package status.conditions

import core.body.Body
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import magic.Element
import status.effects.Effect
import status.effects.EffectBase
import status.effects.EffectP
import status.effects.getPersisted

fun getPersisted(dataObject: Condition): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["element"] = dataObject.element.name
    data["elementStrength"] = dataObject.elementStrength
    data["age"] = dataObject.age
    data["permanent"] = dataObject.permanent
    data["isCritical"] = dataObject.isCritical
    data["isFirstApply"] = dataObject.isFirstApply
    data["effects"] = dataObject.effects.map { getPersisted(it) }
    data["criticalEffects"] = dataObject.criticalEffects.map { getPersisted(it) }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, body: Body): Condition {

    val effects = (data["effects"] as List<Map<String, Any>>).map { status.effects.readFromData(it, body) }
    val criticalEffects = (data["criticalEffects"] as List<Map<String, Any>>).map { status.effects.readFromData(it, body) }
    return Condition(
            data["name"] as String,
            Element.valueOf(data["element"] as String),
            data["elementStrength"] as Int,
            effects,
            criticalEffects,
            data["permanent"] as Boolean,
            data["age"] as Int,
            data["isCritical"] as Boolean,
            data["isFirstApply"] as Boolean
    )
}


@kotlinx.serialization.Serializable
data class ConditionP(
    val name: String,
    val element: Element,
    val elementStrength: Int,
    val effects: List<EffectP>,
    val criticalEffects: List<EffectP>,
    val permanent: Boolean,
    val age: Int,
    val isCritical: Boolean,
    val isFirstApply: Boolean
){
    constructor(b: Condition): this(b.name, b.element, b.elementStrength, b.effects.map { EffectP(it) }, b.criticalEffects.map { EffectP(it) }, b.permanent, b.age, b.isCritical, b.isFirstApply)

    fun parsed(body: Body): Condition {
        return Condition(name, element, elementStrength, effects.map { it.parsed(body) }, criticalEffects.map { it.parsed(body) }, permanent, age, isCritical)
    }
}