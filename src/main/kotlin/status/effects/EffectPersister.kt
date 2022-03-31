package status.effects

import core.body.Body
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


fun getPersisted(dataObject: Effect): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["amount"] = dataObject.amount
    data["duration"] = dataObject.duration
    data["originalValue"] = dataObject.originalValue
    data["bodyParts"] = dataObject.bodyPartTargets.map { it.name }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, body: Body): Effect {
    val bodyParts = (data["bodyParts"] as List<String>).map { body.getPart(it) }
    return EffectManager.getEffect(
            data["name"] as String,
            data["amount"] as Int,
            data["duration"] as Int,
            bodyParts
    )
}

@kotlinx.serialization.Serializable
data class EffectP(
    val base: EffectBase,
    val amount: Int,
    val duration: Int,
    val bodyPartTargets: List<String>
    ){
    constructor(b: Effect): this(b.base, b.amount, b.duration, b.bodyPartTargets.map { it.name })

    fun parsed(body: Body): Effect {
        return Effect(base, amount, duration, bodyPartTargets.map { body.getPart(it) })
    }
}