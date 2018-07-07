package core.gameState

import core.events.Event

class Trigger(private val condition: TriggerCondition, private val event: TriggeredEvent){
    fun evaluateAndExecute(event: Event){
        if (condition.matches(event)){
            this.event.execute(event)
        }
    }
}