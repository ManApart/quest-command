package core.gameState.behavior

class BehaviorBase(val name: String, val condition: TriggerCondition, val events: List<TriggeredEvent> = listOf(), val params: List<String> = listOf())