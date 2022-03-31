package status.stat

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

fun getPersisted(dataObject: LeveledStat): Map<String, Any> {
    //TODO - proper seralization
//    val string = Json.encodeToString(dataObject)
    val string = "{}"
    val data: MutableMap<String, Any> = Json.decodeFromString(string)
    data["version"] = 1

    return data
}

fun readFromData(data: Map<String, Any>): LeveledStat {
    return LeveledStat(
        data["name"] as String,
        data["level"] as Int,
        data["maxMultiplier"] as Int,
        data["expExponential"] as Int,
        data["max"] as Int,
        data["current"] as Int,
        data["xp"] as Double
    )
}

class LeveledStatSerializer : KSerializer<LeveledStat> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LeveledStat", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LeveledStat) =
        encoder.encodeSerializableValue(LeveledStatP.serializer(), LeveledStatP(value))

    override fun deserialize(decoder: Decoder): LeveledStat =
        decoder.decodeSerializableValue(LeveledStatP.serializer()).parsed()
}

@kotlinx.serialization.Serializable
data class LeveledStatP(
    val name: String,
    val level: Int,
    val maxMultiplier: Int,
    val expExponential: Int,
    val max: Int,
    val current: Int,
    val xp: Double,

){
    constructor(b: LeveledStat): this(b.name, b.level, b.getMaxMultiplier(), b.expExponential, b.max, b.current, b.xp)

    fun parsed(): LeveledStat {
        return LeveledStat(name, level, maxMultiplier, expExponential, max, current, xp)
    }
}