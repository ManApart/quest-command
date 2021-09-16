package status.stat

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.target.Target

fun getPersisted(dataObject: LeveledStat): Map<String, Any> {
    val string = jacksonObjectMapper().writeValueAsString(dataObject)
    val data: MutableMap<String, Any> = jacksonObjectMapper().readValue(string)
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