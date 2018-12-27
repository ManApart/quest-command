package core.gameState.behavior

import core.gameState.dataParsing.TriggerCondition
import core.gameState.dataParsing.TriggeredEvent
import core.utility.apply

class BehaviorBase(name: String, condition: TriggerCondition, events: List<TriggeredEvent> = listOf(), params: Map<String, String>) {
    val name = name.apply(params)
    val condition = TriggerCondition(condition, params)
    val events = events.map { TriggeredEvent(it, params) }
}
