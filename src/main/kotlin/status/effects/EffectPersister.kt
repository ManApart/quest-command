package status.effects

import core.body.Body
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import status.stat.LeveledStat
import status.stat.LeveledStatP
import traveling.location.location.Location


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


object EffectPersister : KSerializer<Effect> {
    var body = Body()
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Effect", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Effect) =
        encoder.encodeSerializableValue(EffectP.serializer(), EffectP(value))

    //TODO - how do we pass in locations?
    override fun deserialize(decoder: Decoder): Effect =
        decoder.decodeSerializableValue(EffectP.serializer()).parsed(body)
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