package status.stat

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

fun getPersisted(dataObject: LeveledStat): Map<String, Any> {
    //TODO - proper seralization
//    val string = Json.encodeToString(dataObject)
    val string = "{}"
    val data: MutableMap<String, Any> = Json.decodeFromString(string)
    data["version"] = 1

    return data
}

fun readFromData(data: Map<String, Any>): LeveledStat {
    return LeveledStat(data["name"] as String,
            data["level"] as Int,
            data["maxMultiplier"] as Int,
            data["expExponential"] as Int,
            data["max"] as Int,
            data["current"] as Int,
            data["xp"] as Double
    )
}