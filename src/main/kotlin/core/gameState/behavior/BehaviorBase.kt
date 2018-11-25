package core.gameState.behavior

import core.gameState.dataParsing.TriggerCondition
import core.gameState.dataParsing.TriggeredEvent

class BehaviorBase(val name: String, val condition: TriggerCondition, val events: List<TriggeredEvent> = listOf(), val params: List<String> = listOf())