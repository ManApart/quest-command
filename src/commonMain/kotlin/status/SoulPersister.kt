package status

import core.body.Body
import status.conditions.ConditionP
import status.stat.LeveledStatP

@kotlinx.serialization.Serializable
data class SoulP(
    val stats: List<LeveledStatP> = emptyList(),
    val conditions: List<ConditionP> = emptyList(),

){
    constructor(b: Soul): this(b.getStats().map { LeveledStatP(it)}, b.getConditions().map { ConditionP(it) })

    suspend fun parsed(body: Body): Soul {
        return Soul(stats.map { it.parsed() }.toMutableList()).apply { overrideConditions(conditions.map { it.parsed(body) }) }
    }
}
