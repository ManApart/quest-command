package core.gameState

import core.events.Event

class Trigger(private val condition: TriggerCondition, private val events: List<TriggeredEvent> = listOf()){
    fun evaluateAndExecute(event: Event){
        if (condition.matches(event)){
            this.events.forEach {
                it.execute(event)
            }
        }
    }
}