package status

import core.body.Body
import magic.Element
import status.conditions.Condition
import status.conditions.ConditionP
import status.conditions.getPersisted
import status.effects.EffectP
import status.stat.LeveledStatP
import status.stat.getPersisted

fun getPersisted(dataObject: Soul): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["stats"] = dataObject.getStats().map { getPersisted(it) }
    data["conditions"] = dataObject.getConditions().map { getPersisted(it) }

    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, body: Body): Soul {
    val stats = (data["stats"] as List<Map<String, Any>>).map { status.stat.readFromData(it) }.toMutableList()
    val conditions = (data["conditions"] as List<Map<String, Any>>).map { status.conditions.readFromData(it, body) }

    val soul = Soul(stats)
    soul.overrideConditions(conditions)
    return soul
}

@kotlinx.serialization.Serializable
data class SoulP(
    val stats: List<LeveledStatP>,
    val conditions: List<ConditionP>,

){
    constructor(b: Soul): this(b.getStats().map { LeveledStatP(it)}, b.getConditions().map { ConditionP(it) })

    fun parsed(body: Body): Soul {
        return Soul(stats.map { it.parsed() }.toMutableList()).apply { overrideConditions(conditions.map { it.parsed(body) }) }
    }
}